package de.neuland.jade4j.parser;

import de.neuland.jade4j.compiler.IndentWriter;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.expression.ExpressionHandler;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.parser.node.Node;
import de.neuland.jade4j.template.JadeTemplate;

public class CommentNode extends Node {

	private boolean buffered;
	
	@Override
	public void execute(IndentWriter writer, JadeModel model, JadeTemplate template, ExpressionHandler expressionHandler) throws JadeCompilerException {
		if (!isBuffered()) {
			return;
		}
		writer.newline();
		writer.append("<!--");
		writer.append(value);
		writer.append("-->");
	}

	public boolean isBuffered() {
		return buffered;
	}

	public void setBuffered(boolean buffered) {
		this.buffered = buffered;
	}

}
