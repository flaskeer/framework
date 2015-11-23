package cc.hao.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.hao.field.Reference;
import cc.hao.server.Request;
import cc.hao.util.FileUtil;
import cc.hao.util.ReflectionUtil;
import cc.hao.util.StringKit;

public class View {
	
	public static final Pattern ABSTRACT = Pattern.compile("#abstract (.+?)\n");
	public static final Pattern PROPERTY = Pattern.compile("\\$\\{(.+?)\\}");
	
	private Object model;
	private String content;
	private Request request;
	
	private List<String> lines;
	private int index;
	private String s;
	private StringBuilder block;
	private StringBuilder result = new StringBuilder();
	
	public static String render(Object model,String view,Request request) throws Exception{
		String content = FileUtil.read("views/" + view + ".html").replace("\r","");
		return new View(model, content, request).render();
	}
	
	private String render() throws Exception {
		if(content.startsWith("#extends")){
			content = doExtends(content);
			return new View(model, content, request).render();
		}
		lines = StringKit.split(content, '\n');
		while(index < lines.size()){
			readLine();
			if(s.startsWith("#if")){
				branch();
			}else if(s.startsWith("#loop")){
				loop();
			}else{
				parseLine();
			}
		}
		return result.toString();
	}

	private void parseLine() throws Exception {
		Matcher matcher = PROPERTY.matcher(s);
		while(matcher.find()){
			String group = matcher.group(1);
			Object value = parse(group);
			s = s.replace("{" + group + "}", value.toString());
		}
		if(s.startsWith("#include")){
			include();
		}else{
			result.append(s);
			result.append('\n');
		}
	}

	private void include() throws Exception {
		String path = s.split(" ")[1].substring(1);
		Request req = new Request(request.head(), path);
		result.append(new Controller(req).doRun());
	}

	private void loop() throws Exception {
		Iterable<?> iter = s.contains(" ") ? fromField() : fromModel();
		boolean record = iter.iterator().hasNext();
		if(record){
			for(Object item : iter){
				Reference<?> r = (Reference<?>) item;
				View view = new View(r.get(),block.toString(), request);
				result.append(view.render());
			}
		}
	}
	
	private Iterable<?> fromField() throws Exception{
		String listName = s.split(" ")[1];
		return (Iterable<?>) parse(listName);
	}
	
	private Iterable<?> fromModel(){
		return (Iterable<?>) model;
	}

	private void branch() throws Exception {
		String v = s.split(" ")[1];
		boolean record = v.startsWith("!") ? bool(v.substring(1)) : bool(v);
		readBlock(record);
		if(record){
			View view = new View(model, block.toString(), request);
			result.append(view.render());
		}
	}

	private void readBlock(boolean record) {
		if(record){
			block = new StringBuilder();
		}
		int deep = 1;
		while(true){
			readLine();
			if(s.startsWith("#if") || s.startsWith("#loop")){
				++deep;
			}else if(s.startsWith("#end")){
				--deep;
			}
			if(deep == 0){
				break;
			}
			if(record){
				block.append(s);
				block.append('\n');
			}
		}
	}

	private boolean bool(String var) throws Exception {
		
		return parse(var).equals("true");
	}

	private Object parse(String var) throws Exception {
		if(var.contains(".")){
			String[] parts = var.split("\\.",2);
			return parse(reference(model,parts[0]),parts[1]);
		}
		return parseVar(model,var);
	}

	private Object parseVar(Object model, String var) throws Exception {
		Object value = fromField(model, var);
		if(!StringKit.isEmpty(value)){
			return value;
		}
		String param = request.params().get(var);
		if(param != null){
			return param;
		}
		Object sessionObj = request.session().get(var);
		return sessionObj == null ? "" : sessionObj;
	}

	private Object parse(Object model, String var) throws Exception {
		if(model instanceof Reference<?> && !var.equals("id")){
			Reference<?> reference = (Reference<?>) model;
			return parse(reference.get(), var);
		}
		if(var.contains(".")){
			String[] parts = var.split(".",2);
			model = ReflectionUtil.get(model,var);
			return parse(model, parts[1]);
		}
		return fromField(model,var);
	}

	private Object fromField(Object model, String var) throws Exception {
		Object value = ReflectionUtil.get(model, var);
		
		return value;
	}

	private Reference<?> reference(Object model, String var) throws Exception {
		Object field = ReflectionUtil.get(model, var);
		
		return (Reference<?>) (field == null ? request.session().get(var) : field);
	}

	

	private void readLine() {
		s = lines.get(index++).trim();
		
	}

	private String doExtends(String content) throws Exception {
		String[] parts = content.split("\n", 2);
		String view = parts[0].split(" ")[1];
		String parent = FileUtil.read("views" + view + ".html").replace("\r","");
		if(parent.startsWith("#extends")){
			parent = doExtends(parent);
		}
		return updateParent(parent,overrides(parts[1]));
	}
	
	private Map<String, String> overrides(String body) {
		Map<String,String> overrides = new HashMap<>();
		for(String override : body.split("#override ")){
			if(!override.trim().isEmpty()){
				String[] oParts = override.split("\n",2);
				overrides.put(oParts[0].trim(),oParts[1] + '\n');
			}
		}
		return overrides;
	}

	private String updateParent(String parent,Map<String,String> overrides){
		Matcher matcher = ABSTRACT.matcher(parent);
		if(matcher.find()){
			String name = matcher.group(1).trim();
			String o = overrides.get(name);
			if(o != null){
				parent = parent.replace("#abstract " + name + '\n', o);
			}
		}
		return parent;
	}

	private View(Object model, String content, Request request) {
		this.model = model;
		this.content = content;
		this.request = request;
	}
	
	
}
