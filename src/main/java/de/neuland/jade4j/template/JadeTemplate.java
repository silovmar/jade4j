package de.neuland.jade4j.template;

import de.neuland.jade4j.Jade4J.Mode;
import de.neuland.jade4j.compiler.Compiler;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.expression.ExpressionHandler;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.parser.node.Node;

import java.io.Writer;

public class JadeTemplate {

	private boolean prettyPrint = false;
	private Node rootNode;
	private boolean terse = true;
	private boolean xml = false;
	private TemplateLoader templateLoader;

	public void process(JadeModel model, Writer writer, ExpressionHandler expressionHandler) throws JadeCompilerException {
		Compiler compiler = new Compiler(rootNode);
		compiler.setPrettyPrint(prettyPrint);
		compiler.setTemplate(this);
		compiler.compile(model, writer, expressionHandler);
	}

	public boolean isPrettyPrint() {
		return prettyPrint;
	}

	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	public Node getRootNode() {
		return rootNode;
	}

	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}

	public boolean isTerse() {
		return terse;
	}

	public boolean isXml() {
		return xml;
	}

	public void setTemplateLoader(TemplateLoader templateLoader) {
		this.templateLoader = templateLoader;
	}

	public TemplateLoader getTemplateLoader() {
		return templateLoader;
	}

	public void setMode(Mode mode) {
		xml = false;
		terse = false;
		switch (mode) {
		case HTML:
			terse = true;
			break;
		case XML:
			xml = true;
			break;
		}
	}
}
