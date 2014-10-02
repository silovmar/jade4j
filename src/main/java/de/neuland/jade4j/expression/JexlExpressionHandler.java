    public JexlExpressionHandler() {
        this(new JadeJexlEngine());
        jexl.setCache(MAX_ENTRIES);
    }

	@Override
    public String evaluateStringExpression(String expression, JadeModel model) throws ExpressionException {
	
	@Override
    public void setCache(boolean cache) {

    @Override
    public void clearCache() {
package de.neuland.jade4j.expression;

import de.neuland.jade4j.exceptions.ExpressionException;
import de.neuland.jade4j.model.JadeModel;

public class JexlExpressionHandler implements ExpressionHandler {

/**
 * TODO silovsky: comment
 *
 * @author silovsky
 */
public interface ExpressionHandler {

    Boolean evaluateBooleanExpression(String expression, JadeModel model) throws ExpressionException;

	private JexlEngine jexl;
    Object evaluateExpression(String expression, JadeModel model) throws ExpressionException;

    String evaluateStringExpression(String expression, JadeModel model) throws ExpressionException;

	@Override
    public Object evaluateExpression(String expression, JadeModel model) throws ExpressionException {
    void setCache(boolean cache);

    public JexlExpressionHandler(JexlEngine jexl) {
        this.jexl = jexl;
    }

    @Override
    public Boolean evaluateBooleanExpression(String expression, JadeModel model) throws ExpressionException {
    void clearCache();
}
