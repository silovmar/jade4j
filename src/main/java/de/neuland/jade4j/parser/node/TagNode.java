package de.neuland.jade4j.parser.node;

import de.neuland.jade4j.compiler.IndentWriter;
import de.neuland.jade4j.compiler.Utils;
import de.neuland.jade4j.exceptions.ExpressionException;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.expression.ExpressionHandler;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.template.JadeTemplate;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;

public class TagNode extends AttributedNode {
    private boolean textOnly;
    private Node textNode;
    private Node codeNode;
    private static final String[] selfClosingTags = {"meta", "img", "link", "input", "area", "base", "col", "br", "hr", "source"};
    private boolean selfClosing = false;


    public void setTextOnly(boolean textOnly) {
        this.textOnly = textOnly;

    }

    public void setTextNode(Node textNode) {
        this.textNode = textNode;
    }

    public void setCodeNode(Node codeNode) {
        this.codeNode = codeNode;
    }

    public boolean isTextOnly() {
        return this.textOnly;
    }

    public Node getTextNode() {
        return textNode;
    }

    public boolean hasTextNode() {
        return textNode != null;
    }

    public boolean hasCodeNode() {
        return codeNode != null;
    }

    @Override
    public void execute(IndentWriter writer, JadeModel model, JadeTemplate template, ExpressionHandler expressionHandler) throws JadeCompilerException {
        writer.newline();
        writer.append("<");
        writer.append(name);
        writer.append(attributes(model, template, expressionHandler));
        if (isTerse(template)) {
            writer.append(">");
            return;
        }
        if (selfClosing) {
            boolean empty = isEmpty();
            System.out.println(name);
            System.out.println(empty);
        }
        if (isSelfClosing(template) || (selfClosing && isEmpty())) {
            writer.append("/>");
            return;
        }
        writer.append(">");
        if (hasTextNode()) {
            textNode.execute(writer, model, template, expressionHandler);
        }
        if (hasBlock()) {
            writer.increment();
            block.execute(writer, model, template, expressionHandler);
            writer.decrement();
            writer.newline();
        }
        if (hasCodeNode()) {
            codeNode.execute(writer, model, template, expressionHandler);
        }
        writer.append("</");
        writer.append(name);
        writer.append(">");
    }

    private boolean isEmpty() {
        return !hasBlock() && !hasTextNode() && !hasCodeNode();
    }

    public boolean isTerse(JadeTemplate template) {
        return isSelfClosing(template) && template.isTerse();
    }

    public boolean isSelfClosing(JadeTemplate template) {
        return !template.isXml() && ArrayUtils.contains(selfClosingTags, name);
    }

    private String attributes(JadeModel model, JadeTemplate template, ExpressionHandler expressionHandler) {
        StringBuilder sb = new StringBuilder();

        Map<String, Object> mergedAttributes = mergeInheritedAttributes(model);

        for (Map.Entry<String, Object> entry : mergedAttributes.entrySet()) {
            try {
                sb.append(getAttributeString(entry.getKey(), entry.getValue(), model, template, expressionHandler));
            } catch (ExpressionException e) {
                throw new JadeCompilerException(this, template.getTemplateLoader(), e);
            }
        }

        return sb.toString();
    }

    private String getAttributeString(String name, Object attribute, JadeModel model, JadeTemplate template, ExpressionHandler expressionHandler) throws ExpressionException {
        String value = null;
        if (attribute instanceof String) {
            value = getInterpolatedAttributeValue(name, attribute, model, template, expressionHandler);
        } else if (attribute instanceof Boolean) {
            if ((Boolean) attribute) {
                value = name;
            } else {
                return "";
            }
            if (template.isTerse()) {
                value = null;
            }
        } else if (attribute instanceof ExpressionString) {
            Object expressionValue = evaluateExpression((ExpressionString) attribute, model, expressionHandler);
            if (expressionValue == null) {
                return "";
            }
            // TODO: refactor
            if (expressionValue instanceof Boolean) {
                if ((Boolean) expressionValue) {
                    value = name;
                } else {
                    return "";
                }
                if (template.isTerse()) {
                    value = null;
                }
            } else {
                value = expressionValue.toString();
                value = StringEscapeUtils.escapeHtml4(value);
            }
        } else {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        if (name != null) {
            sb.append(" ").append(name);
            if (value != null) {
                sb.append("=").append('"');
                sb.append(value);
                sb.append('"');
            }
        }
        return sb.toString();
    }

    private Object evaluateExpression(ExpressionString attribute, JadeModel model, ExpressionHandler expressionHandler) throws ExpressionException {
        String expression = ((ExpressionString) attribute).getValue();
        Object result = expressionHandler.evaluateExpression(expression, model, this);
        if (result instanceof ExpressionString) {
            return evaluateExpression((ExpressionString) result, model, expressionHandler);
        }
        return result;
    }

    private String getInterpolatedAttributeValue(String name, Object attribute, JadeModel model, JadeTemplate template, ExpressionHandler expressionHandler) throws JadeCompilerException {
        if (!preparedAttributeValues.containsKey(name)) {
            preparedAttributeValues.put(name, Utils.prepareInterpolate((String) attribute, true));
        }
        List<Object> prepared = preparedAttributeValues.get(name);
        try {
            return Utils.interpolate(prepared, model, expressionHandler, this);
        } catch (ExpressionException e) {
            throw new JadeCompilerException(this, template.getTemplateLoader(), e);
        }
    }

    public void setSelfClosing(boolean selfClosing) {
        this.selfClosing = selfClosing;
    }

    public boolean isSelfClosing() {
        return selfClosing;
    }
}
