package de.neuland.jade4j.parser.node;

import de.neuland.jade4j.compiler.IndentWriter;
import de.neuland.jade4j.exceptions.ExpressionException;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.expression.ExpressionHandler;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.template.JadeTemplate;

import java.util.LinkedList;
import java.util.List;

public class CaseNode extends Node {

    private List<CaseConditionNode> caseConditionNodes = new LinkedList<CaseConditionNode>();

	@Override
	public void execute(IndentWriter writer, JadeModel model, JadeTemplate template, ExpressionHandler expressionHandler) throws JadeCompilerException {
		try {
			for (CaseConditionNode caseConditionNode : caseConditionNodes) {
				if (caseConditionNode.isDefault() || checkCondition(model, caseConditionNode, expressionHandler)) {
					caseConditionNode.execute(writer, model, template, expressionHandler);
					break;
				}
			}
		} catch (ExpressionException e) {
			throw new JadeCompilerException(this, template.getTemplateLoader(), e);
		}
	}

	private Boolean checkCondition(JadeModel model, Node caseConditionNode, ExpressionHandler expressionHandler) throws ExpressionException {
		return expressionHandler.evaluateBooleanExpression(value + " == " + caseConditionNode.getValue(), model, this);
	}

	public void setConditions(List<CaseConditionNode> caseConditionNodes) {
		this.caseConditionNodes = caseConditionNodes;
	}

    public List<CaseConditionNode> getCaseConditionNodes() {
        return caseConditionNodes;
    }

    @Override
    public CaseNode clone() throws CloneNotSupportedException {
        CaseNode clone = (CaseNode) super.clone();

        clone.caseConditionNodes = new LinkedList<CaseConditionNode>();
        for(CaseConditionNode condition : caseConditionNodes) {
            clone.caseConditionNodes.add((CaseConditionNode) condition.clone());
        }

        return clone;
    }
}
