package org.seasar.doma.internal.apt.meta.query;

import java.sql.Blob;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.Context;

public class BlobCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<BlobCreateQueryMeta> {

  public BlobCreateQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement, Blob.class);
  }

  @Override
  public QueryMeta createQueryMeta() {
    var blobFactoryAnnot = ctx.getAnnots().newBlobFactoryAnnot(methodElement);
    if (blobFactoryAnnot == null) {
      return null;
    }
    var queryMeta = new BlobCreateQueryMeta(methodElement);
    queryMeta.setBlobFactoryAnnot(blobFactoryAnnot);
    queryMeta.setQueryKind(QueryKind.BLOB_FACTORY);
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }
}
