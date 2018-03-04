// Generated code from Butter Knife. Do not modify!
package liuliu.qs.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PriceDetailActivity$$ViewBinder<T extends liuliu.qs.ui.PriceDetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558573, "field 'totalPriceTv'");
    target.totalPriceTv = finder.castView(view, 2131558573, "field 'totalPriceTv'");
    view = finder.findRequiredView(source, 2131558662, "field 'juliTv'");
    target.juliTv = finder.castView(view, 2131558662, "field 'juliTv'");
    view = finder.findRequiredView(source, 2131558663, "field 'qibuPriceTv'");
    target.qibuPriceTv = finder.castView(view, 2131558663, "field 'qibuPriceTv'");
    view = finder.findRequiredView(source, 2131558664, "field 'lichengPriceTv'");
    target.lichengPriceTv = finder.castView(view, 2131558664, "field 'lichengPriceTv'");
    view = finder.findRequiredView(source, 2131558524, "field 'bar'");
    target.bar = finder.castView(view, 2131558524, "field 'bar'");
  }

  @Override public void unbind(T target) {
    target.totalPriceTv = null;
    target.juliTv = null;
    target.qibuPriceTv = null;
    target.lichengPriceTv = null;
    target.bar = null;
  }
}
