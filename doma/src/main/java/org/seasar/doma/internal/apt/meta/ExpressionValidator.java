package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.internal.expr.node.AddOperatorNode;
import org.seasar.doma.internal.expr.node.AndOperatorNode;
import org.seasar.doma.internal.expr.node.CommaOperatorNode;
import org.seasar.doma.internal.expr.node.DivideOperatorNode;
import org.seasar.doma.internal.expr.node.EmptyNode;
import org.seasar.doma.internal.expr.node.EqOperatorNode;
import org.seasar.doma.internal.expr.node.ExpressionLocation;
import org.seasar.doma.internal.expr.node.ExpressionNode;
import org.seasar.doma.internal.expr.node.ExpressionNodeVisitor;
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

public class ExpressionValidator implements ExpressionNodeVisitor<Void, Void> {

    protected final ProcessingEnvironment env;

    protected final QueryMeta queryMeta;

    protected final ExecutableElement method;

    protected final String path;

    protected final Map<ExpressionNode, TypeMirrorHolder> typeMirrorHolderMap = new HashMap<ExpressionNode, TypeMirrorHolder>();

    public ExpressionValidator(ProcessingEnvironment env, QueryMeta queryMeta,
            ExecutableElement method, String path) {
        assertNotNull(env, queryMeta, method, path);
        this.env = env;
        this.queryMeta = queryMeta;
        this.method = method;
        this.path = path;
    }

    public void validate(ExpressionNode node) {
        node.accept(this, null);
    }

    @Override
    public Void visitEqOperatorNode(EqOperatorNode node, Void p) {
        node.getLeftNode().accept(this, p);
        node.getRightNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitNeOperatorNode(NeOperatorNode node, Void p) {
        node.getLeftNode().accept(this, p);
        node.getRightNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitGeOperatorNode(GeOperatorNode node, Void p) {
        node.getLeftNode().accept(this, p);
        node.getRightNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitGtOperatorNode(GtOperatorNode node, Void p) {
        node.getLeftNode().accept(this, p);
        node.getRightNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitLeOperatorNode(LeOperatorNode node, Void p) {
        node.getLeftNode().accept(this, p);
        node.getRightNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitLtOperatorNode(LtOperatorNode node, Void p) {
        node.getLeftNode().accept(this, p);
        node.getRightNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitAndOperatorNode(AndOperatorNode node, Void p) {
        node.getLeftNode().accept(this, p);
        node.getRightNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitOrOperatorNode(OrOperatorNode node, Void p) {
        node.getLeftNode().accept(this, p);
        node.getRightNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitNotOperatorNode(NotOperatorNode node, Void p) {
        node.getNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitAddOperatorNode(AddOperatorNode node, Void p) {
        node.getLeftNode().accept(this, p);
        node.getRightNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitSubtractOperatorNode(SubtractOperatorNode node, Void p) {
        node.getLeftNode().accept(this, p);
        node.getRightNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitMultiplyOperatorNode(MultiplyOperatorNode node, Void p) {
        node.getLeftNode().accept(this, p);
        node.getRightNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitDivideOperatorNode(DivideOperatorNode node, Void p) {
        node.getLeftNode().accept(this, p);
        node.getRightNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitLiteralNode(LiteralNode node, Void p) {
        return null;
    }

    @Override
    public Void visitParensNode(ParensNode node, Void p) {
        node.getNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitNewOperatorNode(NewOperatorNode node, Void p) {
        node.getParametersNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitMethodOperatorNode(MethodOperatorNode node, Void p) {
        node.getTargetObjectNode().accept(this, p);
        node.getParametersNode().accept(this, p);

        TypeMirrorHolder typeMirrorHolder = typeMirrorHolderMap.get(node
                .getTargetObjectNode());
        if (typeMirrorHolder == null) {
            return null;
        }
        String methodName = node.getName();
        ExecutableElement foundMethod = findMethod(node, typeMirrorHolder,
                methodName);
        if (foundMethod == null) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(DomaMessageCode.DOMA4071, env, method,
                    location.getExpression(), location.getPosition(),
                    typeMirrorHolder.name, typeMirrorHolder.typeMirror
                            .toString(), methodName);
        }
        if (!new ParameterInquirer().exists(node.getParametersNode())) {
            typeMirrorHolderMap.put(node, new TypeMirrorHolder(methodName,
                    foundMethod.getReturnType()));
        }
        return null;
    }

    public ExecutableElement findMethod(MethodOperatorNode node,
            TypeMirrorHolder typeMirrorHolder, String methodName) {
        TypeElement typeElement = null;
        for (TypeMirror t = typeMirrorHolder.typeMirror; t.getKind() != TypeKind.NONE; t = typeElement
                .getSuperclass()) {
            typeElement = TypeUtil.toTypeElement(t, env);
            if (typeElement == null) {
                ExpressionLocation location = node.getLocation();
                throw new AptException(DomaMessageCode.DOMA4072, env, method,
                        location.getExpression(), location.getPosition(),
                        typeMirrorHolder.name, typeMirrorHolder.typeMirror
                                .toString());
            }
            for (ExecutableElement e : ElementFilter.methodsIn(typeElement
                    .getEnclosedElements())) {
                if (e.getSimpleName().contentEquals(methodName)) {
                    return e;
                }
            }
        }
        return null;
    }

    @Override
    public Void visitVariableNode(VariableNode node, Void p) {
        String variableName = node.getName();
        TypeMirror typeMirror = queryMeta.getExpressionParameterTypes().get(
                variableName);
        if (typeMirror == null) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(DomaMessageCode.DOMA4067, env, method,
                    location.getExpression(), location.getPosition(),
                    variableName);
        }
        typeMirrorHolderMap.put(node, new TypeMirrorHolder(variableName,
                typeMirror));
        return null;
    }

    @Override
    public Void visitCommaOperatorNode(CommaOperatorNode node, Void p) {
        return null;
    }

    @Override
    public Void visitEmptyNode(EmptyNode node, Void p) {
        return null;
    }

    protected static class TypeMirrorHolder {

        protected final String name;

        protected final TypeMirror typeMirror;

        public TypeMirrorHolder(String name, TypeMirror typeMirror) {
            assertNotNull(name, typeMirror);
            this.name = name;
            this.typeMirror = typeMirror;
        }
    }

    protected static class ParameterInquirer implements
            ExpressionNodeVisitor<Boolean, Void> {

        public boolean exists(ExpressionNode node) {
            return node.accept(this, null);
        }

        @Override
        public Boolean visitAddOperatorNode(AddOperatorNode node, Void p) {
            return true;
        }

        @Override
        public Boolean visitAndOperatorNode(AndOperatorNode node, Void p) {
            return true;
        }

        @Override
        public Boolean visitCommaOperatorNode(CommaOperatorNode node, Void p) {
            return true;
        }

        @Override
        public Boolean visitDivideOperatorNode(DivideOperatorNode node, Void p) {
            return true;
        }

        @Override
        public Boolean visitEmptyNode(EmptyNode node, Void p) {
            return false;
        }

        @Override
        public Boolean visitEqOperatorNode(EqOperatorNode node, Void p) {
            return true;
        }

        @Override
        public Boolean visitGeOperatorNode(GeOperatorNode node, Void p) {
            return true;
        }

        @Override
        public Boolean visitGtOperatorNode(GtOperatorNode node, Void p) {
            return true;
        }

        @Override
        public Boolean visitLeOperatorNode(LeOperatorNode node, Void p) {
            return true;
        }

        @Override
        public Boolean visitLiteralNode(LiteralNode node, Void p) {
            return true;
        }

        @Override
        public Boolean visitLtOperatorNode(LtOperatorNode node, Void p) {
            return true;
        }

        @Override
        public Boolean visitMethodOperatorNode(MethodOperatorNode node, Void p) {
            return true;
        }

        @Override
        public Boolean visitMultiplyOperatorNode(MultiplyOperatorNode node,
                Void p) {
            return true;
        }

        @Override
        public Boolean visitNeOperatorNode(NeOperatorNode node, Void p) {
            return true;
        }

        @Override
        public Boolean visitNewOperatorNode(NewOperatorNode node, Void p) {
            return true;
        }

        @Override
        public Boolean visitNotOperatorNode(NotOperatorNode node, Void p) {
            return true;
        }

        @Override
        public Boolean visitOrOperatorNode(OrOperatorNode node, Void p) {
            return true;
        }

        @Override
        public Boolean visitParensNode(ParensNode node, Void p) {
            return node.accept(this, p);
        }

        @Override
        public Boolean visitSubtractOperatorNode(SubtractOperatorNode node,
                Void p) {
            return true;
        }

        @Override
        public Boolean visitVariableNode(VariableNode node, Void p) {
            return true;
        }

    }

}
