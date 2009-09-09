package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
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

    protected final Map<ExpressionNode, TypeContext> typeContextMap = new HashMap<ExpressionNode, TypeContext>();

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
    public Void visitCommaOperatorNode(CommaOperatorNode node, Void p) {
        return null;
    }

    @Override
    public Void visitEmptyNode(EmptyNode node, Void p) {
        return null;
    }

    @Override
    public Void visitMethodOperatorNode(MethodOperatorNode node, Void p) {
        node.getTargetObjectNode().accept(this, p);
        node.getParametersNode().accept(this, p);

        TypeContext typeContext = typeContextMap.get(node
                .getTargetObjectNode());
        if (typeContext == null) {
            return null;
        }
        TypeContext resultContext = typeContext.createResultContext(node);
        if (resultContext != null) {
            typeContextMap.put(node, resultContext);
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
        typeContextMap.put(node, new TypeContext(env, method,
                variableName, typeMirror));
        return null;
    }

    protected static class TypeContext {

        protected final ProcessingEnvironment env;

        protected final ExecutableElement executableElement;

        protected final String name;

        protected final TypeMirror typeMirror;

        protected final Map<String, Map<TypeMirror, TypeMirror>> mapOfTypeParamMap = new HashMap<String, Map<TypeMirror, TypeMirror>>();

        public TypeContext(ProcessingEnvironment env,
                ExecutableElement executableElement, String name,
                TypeMirror typeMirror) {
            assertNotNull(env, executableElement, name, typeMirror);
            this.env = env;
            this.executableElement = executableElement;
            this.name = name;
            this.typeMirror = typeMirror;
            fillMapOfTypeParamMap(typeMirror, mapOfTypeParamMap);
        }

        protected void fillMapOfTypeParamMap(TypeMirror typeMirror,
                Map<String, Map<TypeMirror, TypeMirror>> mapOfTypeParamMap) {
            TypeElement typeElement = TypeUtil.toTypeElement(typeMirror, env);
            if (typeElement == null) {
                return;
            }
            mapOfTypeParamMap.put(typeElement.getQualifiedName().toString(),
                    TypeUtil.createTypeParameterMap(typeElement, typeMirror,
                            env));
            for (TypeMirror superType : env.getTypeUtils().directSupertypes(
                    typeMirror)) {
                TypeElement superElement = TypeUtil.toTypeElement(superType,
                        env);
                if (superElement == null) {
                    continue;
                }
                if (mapOfTypeParamMap.containsKey(superElement
                        .getQualifiedName().toString())) {
                    continue;
                }
                mapOfTypeParamMap.put(superElement.getQualifiedName()
                        .toString(), TypeUtil.createTypeParameterMap(
                        superElement, superType, env));
                fillMapOfTypeParamMap(superType, mapOfTypeParamMap);
            }
        }

        public TypeContext createResultContext(MethodOperatorNode node) {
            int parameterSize = node.getParametersNode().accept(
                    new ParameterCounter(), null);
            List<MethodContext> methodContexts = getMethodContexts(node,
                    parameterSize);
            if (methodContexts.size() == 0) {
                ExpressionLocation location = node.getLocation();
                throw new AptException(DomaMessageCode.DOMA4071, env,
                        executableElement, location.getExpression(), location
                                .getPosition(), name, typeMirror.toString(),
                        parameterSize, node.getName());
            }
            if (methodContexts.size() == 1) {
                MethodContext methodContext = methodContexts.get(0);
                return new TypeContext(env, executableElement, node.getName(),
                        methodContext.getReturnType());
            }
            return null;
        }

        protected List<MethodContext> getMethodContexts(
                MethodOperatorNode node, int parameterSize) {
            List<MethodContext> candidate = new LinkedList<MethodContext>();
            for (Map.Entry<String, Map<TypeMirror, TypeMirror>> e : mapOfTypeParamMap
                    .entrySet()) {
                TypeElement typeElement = env.getElementUtils().getTypeElement(
                        e.getKey());
                if (typeElement == null) {
                    ExpressionLocation location = node.getLocation();
                    throw new AptException(DomaMessageCode.DOMA4072, env,
                            executableElement, location.getExpression(),
                            location.getPosition(), name, e.getKey());
                }
                for (ExecutableElement method : ElementFilter
                        .methodsIn(typeElement.getEnclosedElements())) {
                    if (!method.getModifiers().contains(Modifier.PUBLIC)) {
                        continue;
                    }
                    if (!method.getSimpleName().contentEquals(node.getName())) {
                        continue;
                    }
                    if (method.getReturnType().getKind() == TypeKind.VOID) {
                        continue;
                    }
                    if (method.getParameters().size() != parameterSize) {
                        continue;
                    }
                    candidate.add(new MethodContext(env, typeElement, method, e
                            .getValue()));
                }
            }

            List<MethodContext> copy = new LinkedList<MethodContext>(candidate);
            for (Iterator<MethodContext> it = candidate.iterator(); it
                    .hasNext();) {
                MethodContext overridden = it.next();
                for (MethodContext overrider : copy) {
                    if (env.getElementUtils().overrides(overrider.method,
                            overridden.method, overrider.type)) {
                        it.remove();
                    }
                }
            }
            return candidate;
        }
    }

    protected static class MethodContext {

        protected final ProcessingEnvironment env;

        protected final TypeElement type;

        protected final ExecutableElement method;

        protected final Map<TypeMirror, TypeMirror> typeParameterMap;

        protected MethodContext(ProcessingEnvironment env, TypeElement type,
                ExecutableElement method,
                Map<TypeMirror, TypeMirror> typeParameterMap) {
            assertNotNull(env, type, method, typeParameterMap);
            this.env = env;
            this.type = type;
            this.method = method;
            this.typeParameterMap = typeParameterMap;
        }

        public TypeMirror getReturnType() {
            return TypeUtil.resolveTypeParameter(typeParameterMap, method
                    .getReturnType());
        }
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
