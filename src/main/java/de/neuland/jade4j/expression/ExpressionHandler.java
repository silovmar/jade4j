package de.neuland.jade4j.expression;

import de.neuland.jade4j.exceptions.ExpressionException;
import de.neuland.jade4j.model.JadeModel;

/**
 * TODO silovsky: comment
 *
 * @author silovsky
 */
public interface ExpressionHandler {

    Boolean evaluateBooleanExpression(String expression, JadeModel model) throws ExpressionException;

    Object evaluateExpression(String expression, JadeModel model) throws ExpressionException;

    String evaluateStringExpression(String expression, JadeModel model) throws ExpressionException;

    void setCache(boolean cache);

    void clearCache();
}
