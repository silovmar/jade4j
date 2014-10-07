package de.neuland.jade4j.parser.node;

import de.neuland.jade4j.compiler.IndentWriter;
import de.neuland.jade4j.exceptions.ExpressionException;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.expression.ExpressionHandler;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.template.JadeTemplate;

public class WhileNode extends Node {

	@Override
	public void execute(IndentWriter writer, JadeModel model, JadeTemplate template, ExpressionHandler expressionHandler, Node parent) throws JadeCompilerException {
		try {
			model.pushScope();
            while (expressionHandler.evaluateBooleanExpression(value, model, parent)) {
				block.execute(writer, model, template, expressionHandler, this);
			}
			model.popScope();
		} catch (ExpressionException e) {
			throw new JadeCompilerException(this, template.getTemplateLoader(), e);
		}
	}
}
