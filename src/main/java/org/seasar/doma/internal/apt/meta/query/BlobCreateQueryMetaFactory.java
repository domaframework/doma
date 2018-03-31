package org.seasar.doma.internal.apt.meta.query;

import java.sql.Blob;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.reflection.BlobFactoryReflection;

public class BlobCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<BlobCreateQueryMeta> {

  public BlobCreateQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement, Blob.class);
  }

  @Override
  public QueryMeta createQueryMeta() {
    BlobFactoryReflection blobFactoryReflection =
        ctx.getReflections().newBlobFactoryReflection(methodElement);
    if (blobFactoryReflection == null) {
      return null;
    }
    BlobCreateQueryMeta queryMeta = new BlobCreateQueryMeta(methodElement);
    queryMeta.setBlobFactoryReflection(blobFactoryReflection);
    queryMeta.setQueryKind(QueryKind.BLOB_FACTORY);
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }
}
