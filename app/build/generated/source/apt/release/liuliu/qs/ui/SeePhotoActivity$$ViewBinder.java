// Generated code from Butter Knife. Do not modify!
package liuliu.qs.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SeePhotoActivity$$ViewBinder<T extends liuliu.qs.ui.SeePhotoActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558553, "field 'BigPhoto'");
    target.BigPhoto = finder.castView(view, 2131558553, "field 'BigPhoto'");
    view = finder.findRequiredView(source, 2131558550, "field 'BackTv'");
    target.BackTv = finder.castView(view, 2131558550, "field 'BackTv'");
    view = finder.findRequiredView(source, 2131558552, "field 'imgNumberTv'");
    target.imgNumberTv = finder.castView(view, 2131558552, "field 'imgNumberTv'");
  }

  @Override public void unbind(T target) {
    target.BigPhoto = null;
    target.BackTv = null;
    target.imgNumberTv = null;
  }
}
