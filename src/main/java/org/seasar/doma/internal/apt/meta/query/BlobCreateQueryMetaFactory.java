package org.seasar.doma.internal.apt.meta.query;

import java.sql.Blob;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.BlobFactoryAnnot;

public class BlobCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<BlobCreateQueryMeta> {

  public BlobCreateQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement, Blob.class);
  }

  @Override
  public QueryMeta createQueryMeta() {
    BlobFactoryAnnot blobFactoryAnnot = ctx.getAnnots().newBlobFactoryAnnot(methodElement);
    if (blobFactoryAnnot == null) {
      return null;
    }
    BlobCreateQueryMeta queryMeta = new BlobCreateQueryMeta(methodElement);
    queryMeta.setBlobFactoryAnnot(blobFactoryAnnot);
    queryMeta.setQueryKind(QueryKind.BLOB_FACTORY);
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }
}
