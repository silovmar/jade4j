package de.neuland.jade4j.expression;

import de.neuland.jade4j.exceptions.ExpressionException;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.parser.node.Node;

/**
 * TODO silovsky: comment
 *
 * @author silovsky
 */
public interface ExpressionHandler {

    Boolean evaluateBooleanExpression(String expression, JadeModel model, Node callNode) throws ExpressionException;

    Object evaluateExpression(String expression, JadeModel model, Node callNode) throws ExpressionException;

    String evaluateStringExpression(String expression, JadeModel model, Node callNode) throws ExpressionException;

    void setCache(boolean cache);

    void clearCache();
}
