package de.neuland.jade4j.expression;

import de.neuland.jade4j.exceptions.ExpressionException;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.parser.node.Node;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JadeJexlEngine;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

public class JexlExpressionHandler implements ExpressionHandler {

    private static final int MAX_ENTRIES = 5000;

    private JexlEngine jexl;

    public JexlExpressionHandler() {
        this(new JadeJexlEngine());
        jexl.setCache(MAX_ENTRIES);
    }

    public JexlExpressionHandler(JexlEngine jexl) {
        this.jexl = jexl;
    }

    @Override
    public Boolean evaluateBooleanExpression(String expression, JadeModel model, Node callNode) throws ExpressionException {
        return BooleanUtil.convert(evaluateExpression(expression, model, callNode));
    }

    @Override
    public Object evaluateExpression(String expression, JadeModel model, Node callNode) throws ExpressionException {
        try {
            Expression e = jexl.createExpression(expression);
            return e.evaluate(new MapContext(model));
        } catch (Exception e) {
            throw new ExpressionException(expression, e);
        }
    }

    @Override
    public String evaluateStringExpression(String expression, JadeModel model, Node callNode) throws ExpressionException {
        Object result = evaluateExpression(expression, model, callNode);
        return result == null ? "" : result.toString();
    }

    @Override
    public void setCache(boolean cache) {
        jexl.setCache(cache ? MAX_ENTRIES : 0);
    }

    @Override
    public void clearCache() {
        jexl.clearCache();
    }
}
