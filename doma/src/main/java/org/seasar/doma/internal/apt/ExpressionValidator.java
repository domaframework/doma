package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.declaration.FieldDeclaration;
import org.seasar.doma.internal.apt.declaration.MethodDeclaration;
import org.seasar.doma.internal.apt.declaration.TypeDeclaration;
import org.seasar.doma.internal.expr.node.AddOperatorNode;
import org.seasar.doma.internal.expr.node.AndOperatorNode;
import org.seasar.doma.internal.expr.node.CommaOperatorNode;
import org.seasar.doma.internal.expr.node.DivideOperatorNode;
import org.seasar.doma.internal.expr.node.EmptyNode;
import org.seasar.doma.internal.expr.node.EqOperatorNode;
import org.seasar.doma.internal.expr.node.ExpressionLocation;
import org.seasar.doma.internal.expr.node.ExpressionNode;
import org.seasar.doma.internal.expr.node.ExpressionNodeVisitor;
import org.seasar.doma.internal.expr.node.FieldOperatorNode;
import org.seasar.doma.internal.expr.node.GeOperatorNode;
import org.seasar.doma.internal.expr.node.GtOperatorNode;
import org.seasar.doma.internal.expr.node.LeOperatorNode;
import org.seasar.doma.internal.expr.node.LiteralNode;
import org.seasar.doma.internal.expr.node.LtOperatorNode;
import org.seasar.doma.internal.expr.node.MethodOperatorNode;
import org.seasar.doma.internal.expr.node.MultiplyOperatorNode;
import org.seasar.doma.internal.expr.node.NeOperatorNode;
import org.seasar.doma.internal.expr.node.NewOperatorNode;
import org.seasar.doma.internal.expr.node.NotOperatorNode;
import org.seasar.doma.internal.expr.node.OrOperatorNode;
import org.seasar.doma.internal.expr.node.ParensNode;
import org.seasar.doma.internal.expr.node.SubtractOperatorNode;
import org.seasar.doma.internal.expr.node.VariableNode;
import org.seasar.doma.message.DomaMessageCode;

public class ExpressionValidator implements
        ExpressionNodeVisitor<Boolean, Void> {

    protected final ProcessingEnvironment env;

    protected final ExecutableElement methodElement;

    protected final Map<String, TypeMirror> parameterTypeMap;

    protected final Map<ExpressionNode, TypeDeclaration> typeDeclarationMap = new HashMap<ExpressionNode, TypeDeclaration>();

    public ExpressionValidator(ProcessingEnvironment env,
            ExecutableElement methodElement, Map<String, TypeMirror> parameterTypeMap) {
        assertNotNull(env, methodElement, parameterTypeMap);
        this.env = env;
        this.methodElement = methodElement;
        this.parameterTypeMap = new HashMap<String, TypeMirror>(
                parameterTypeMap);
    }

    public boolean validate(ExpressionNode node) {
        return node.accept(this, null);
    }

    @Override
    public Boolean visitEqOperatorNode(EqOperatorNode node, Void p) {
        return node.getLeftNode().accept(this, p)
                && node.getRightNode().accept(this, p);
    }

    @Override
    public Boolean visitNeOperatorNode(NeOperatorNode node, Void p) {
        return node.getLeftNode().accept(this, p)
                && node.getRightNode().accept(this, p);
    }

    @Override
    public Boolean visitGeOperatorNode(GeOperatorNode node, Void p) {
        return node.getLeftNode().accept(this, p)
                && node.getRightNode().accept(this, p);
    }

    @Override
    public Boolean visitGtOperatorNode(GtOperatorNode node, Void p) {
        return node.getLeftNode().accept(this, p)
                && node.getRightNode().accept(this, p);
    }

    @Override
    public Boolean visitLeOperatorNode(LeOperatorNode node, Void p) {
        return node.getLeftNode().accept(this, p)
                && node.getRightNode().accept(this, p);
    }

    @Override
    public Boolean visitLtOperatorNode(LtOperatorNode node, Void p) {
        return node.getLeftNode().accept(this, p)
                && node.getRightNode().accept(this, p);
    }

    @Override
    public Boolean visitAndOperatorNode(AndOperatorNode node, Void p) {
        return node.getLeftNode().accept(this, p)
                && node.getRightNode().accept(this, p);
    }

    @Override
    public Boolean visitOrOperatorNode(OrOperatorNode node, Void p) {
        return node.getLeftNode().accept(this, p)
                && node.getRightNode().accept(this, p);
    }

    @Override
    public Boolean visitNotOperatorNode(NotOperatorNode node, Void p) {
        return node.getNode().accept(this, p);
    }

    @Override
    public Boolean visitAddOperatorNode(AddOperatorNode node, Void p) {
        return node.getLeftNode().accept(this, p)
                && node.getRightNode().accept(this, p);
    }

    @Override
    public Boolean visitSubtractOperatorNode(SubtractOperatorNode node, Void p) {
        return node.getLeftNode().accept(this, p)
                && node.getRightNode().accept(this, p);
    }

    @Override
    public Boolean visitMultiplyOperatorNode(MultiplyOperatorNode node, Void p) {
        return node.getLeftNode().accept(this, p)
                && node.getRightNode().accept(this, p);
    }

    @Override
    public Boolean visitDivideOperatorNode(DivideOperatorNode node, Void p) {
        return node.getLeftNode().accept(this, p)
                && node.getRightNode().accept(this, p);
    }

    @Override
    public Boolean visitLiteralNode(LiteralNode node, Void p) {
        return true;
    }

    @Override
    public Boolean visitParensNode(ParensNode node, Void p) {
        node.getNode().accept(this, p);
        return true;
    }

    @Override
    public Boolean visitNewOperatorNode(NewOperatorNode node, Void p) {
        node.getParametersNode().accept(this, p);
        return true;
    }

    @Override
    public Boolean visitCommaOperatorNode(CommaOperatorNode node, Void p) {
        return true;
    }

    @Override
    public Boolean visitEmptyNode(EmptyNode node, Void p) {
        return true;
    }

    @Override
    public Boolean visitMethodOperatorNode(MethodOperatorNode node, Void p) {
        node.getTargetObjectNode().accept(this, p);
        node.getParametersNode().accept(this, p);

        TypeDeclaration typeDeclaration = typeDeclarationMap.get(node
                .getTargetObjectNode());
        if (typeDeclaration == null) {
            return false;
        }
        String methodName = node.getName();
        int parameterSize = node.getParametersNode().accept(
                new ParameterCounter(), null);
        List<MethodDeclaration> methodDeclarations = typeDeclaration
                .getMethodDeclarations(methodName, parameterSize);
        if (methodDeclarations.size() == 0) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(DomaMessageCode.DOMA4071, env, methodElement,
                    location.getExpression(), location.getPosition(),
                    methodName, typeDeclaration.getTypeElement()
                            .getQualifiedName(), parameterSize, methodName);
        }
        if (methodDeclarations.size() == 1) {
            MethodDeclaration methodDeclaration = methodDeclarations.get(0);
            TypeDeclaration returnTypeDeclaration = methodDeclaration
                    .getReturnTypeDeclaration();
            if (returnTypeDeclaration != null) {
                typeDeclarationMap.put(node, returnTypeDeclaration);
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean visitFieldOperatorNode(FieldOperatorNode node, Void p) {
        node.getTargetObjectNode().accept(this, p);

        TypeDeclaration typeDeclaration = typeDeclarationMap.get(node
                .getTargetObjectNode());
        if (typeDeclaration == null) {
            return false;
        }
        String fieldName = node.getName();
        FieldDeclaration fieldDeclarations = typeDeclaration
                .getFieldDeclaration(fieldName);
        if (fieldDeclarations != null) {
            TypeDeclaration fieldTypeDeclaration = fieldDeclarations
                    .getTypeDeclaration();
            if (fieldTypeDeclaration != null) {
                typeDeclarationMap.put(node, fieldTypeDeclaration);
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean visitVariableNode(VariableNode node, Void p) {
        String variableName = node.getName();
        TypeMirror typeMirror = parameterTypeMap.get(variableName);
        if (typeMirror == null) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(DomaMessageCode.DOMA4067, env, methodElement,
                    location.getExpression(), location.getPosition(),
                    variableName);
        }
        TypeElement typeElement = TypeUtil.toTypeElement(typeMirror, env);
        if (typeElement == null) {
            return false;
        }
        TypeDeclaration typeDeclaration = TypeDeclaration.newInstance(
                typeElement, env);
        typeDeclarationMap.put(node, typeDeclaration);
        return true;
    }

    protected static class ParameterCounter implements
            ExpressionNodeVisitor<Integer, Void> {

        int count;

        @Override
        public Integer visitAddOperatorNode(AddOperatorNode node, Void p) {
            return count;
        }

        @Override
        public Integer visitAndOperatorNode(AndOperatorNode node, Void p) {
            return count;
        }

        @Override
        public Integer visitCommaOperatorNode(CommaOperatorNode node, Void p) {
            count++;
            for (ExpressionNode child : node.getNodes()) {
                child.accept(this, p);
            }
            return count;
        }

        @Override
        public Integer visitDivideOperatorNode(DivideOperatorNode node, Void p) {
            return count;
        }

        @Override
        public Integer visitEmptyNode(EmptyNode node, Void p) {
            count = 0;
            return count;
        }

        @Override
        public Integer visitEqOperatorNode(EqOperatorNode node, Void p) {
            return count;
        }

        @Override
        public Integer visitGeOperatorNode(GeOperatorNode node, Void p) {
            return count;
        }

        @Override
        public Integer visitGtOperatorNode(GtOperatorNode node, Void p) {
            return count;
        }

        @Override
        public Integer visitLeOperatorNode(LeOperatorNode node, Void p) {
            return count;
        }

        @Override
        public Integer visitLiteralNode(LiteralNode node, Void p) {
            return count;
        }

        @Override
        public Integer visitLtOperatorNode(LtOperatorNode node, Void p) {
            return count;
        }

        @Override
        public Integer visitMethodOperatorNode(MethodOperatorNode node, Void p) {
            return count;
        }

        @Override
        public Integer visitFieldOperatorNode(FieldOperatorNode node, Void p) {
            return count;
        }

        @Override
        public Integer visitMultiplyOperatorNode(MultiplyOperatorNode node,
                Void p) {
            return count;
        }

        @Override
        public Integer visitNeOperatorNode(NeOperatorNode node, Void p) {
            return count;
        }

        @Override
        public Integer visitNewOperatorNode(NewOperatorNode node, Void p) {
            return count;
        }

        @Override
        public Integer visitNotOperatorNode(NotOperatorNode node, Void p) {
            return count;
        }

        @Override
        public Integer visitOrOperatorNode(OrOperatorNode node, Void p) {
            return count;
        }

        @Override
        public Integer visitParensNode(ParensNode node, Void p) {
            count++;
            return node.getNode().accept(this, p);
        }

        @Override
        public Integer visitSubtractOperatorNode(SubtractOperatorNode node,
                Void p) {
            return count;
        }

        @Override
        public Integer visitVariableNode(VariableNode node, Void p) {
            return count;
        }

    }

}
