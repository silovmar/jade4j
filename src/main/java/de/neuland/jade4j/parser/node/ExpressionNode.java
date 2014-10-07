package de.neuland.jade4j.parser.node;

import de.neuland.jade4j.compiler.IndentWriter;
import de.neuland.jade4j.exceptions.ExpressionException;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.expression.ExpressionHandler;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.template.JadeTemplate;

import org.apache.commons.lang3.StringEscapeUtils;

public class ExpressionNode extends Node {

	private boolean escape;
	private boolean buffer;

	public void setEscape(boolean escape) {
		this.escape = escape;
	}

	public void setBuffer(boolean buffer) {
		this.buffer = buffer;
	}

	@Override
	public void execute(IndentWriter writer, JadeModel model, JadeTemplate template, ExpressionHandler expressionHandler) throws JadeCompilerException {
		try {
			Object result = expressionHandler.evaluateStringExpression(getValue(), model, callNode);
			if (result == null || !buffer) {
				return;
			}
			String string = result.toString();
			if (escape) {
				string = StringEscapeUtils.escapeHtml4(string);
			}
			writer.append(string);

            if (hasBlock()) {
                writer.increment();
                block.execute(writer, model, template, expressionHandler);
                writer.decrement();
                writer.newline();
            }

		} catch (ExpressionException e) {
			throw new JadeCompilerException(this, template.getTemplateLoader(), e);
		}
	}

	@Override
	public void setValue(String value) {
		super.setValue(value.trim());
	}
}
