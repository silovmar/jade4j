package de.neuland.jade4j.compiler;

import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.expression.ExpressionHandler;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.parser.node.Node;
import de.neuland.jade4j.template.JadeTemplate;

import java.io.StringWriter;
import java.io.Writer;

public class Compiler {

	private final Node rootNode;
	private boolean prettyPrint;
	private JadeTemplate template = new JadeTemplate();

	public Compiler(Node rootNode) {
		this.rootNode = rootNode;
	}

	public String compileToString(JadeModel model, ExpressionHandler expressionHandler) throws JadeCompilerException {
		StringWriter writer = new StringWriter();
		compile(model, writer, expressionHandler);
		return writer.toString();
	}

	public void compile(JadeModel model, Writer w, ExpressionHandler expressionHandler) throws JadeCompilerException {
		IndentWriter writer = new IndentWriter(w);
		writer.setUseIndent(prettyPrint);
		rootNode.execute(writer, model, template, expressionHandler, rootNode);
	}

	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	public void setTemplate(JadeTemplate jadeTemplate) {
		this.template = jadeTemplate;
	}
}