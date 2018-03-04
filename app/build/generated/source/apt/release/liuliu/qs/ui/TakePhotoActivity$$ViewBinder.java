// Generated code from Butter Knife. Do not modify!
package liuliu.qs.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class TakePhotoActivity$$ViewBinder<T extends liuliu.qs.ui.TakePhotoActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558675, "field 'SendPhotoLl'");
    target.SendPhotoLl = finder.castView(view, 2131558675, "field 'SendPhotoLl'");
    view = finder.findRequiredView(source, 2131558676, "field 'SendphotoButton'");
    target.SendphotoButton = finder.castView(view, 2131558676, "field 'SendphotoButton'");
    view = finder.findRequiredView(source, 2131558681, "field 'RetakeButton'");
    target.RetakeButton = finder.castView(view, 2131558681, "field 'RetakeButton'");
    view = finder.findRequiredView(source, 2131558677, "field 'PhotoImageview'");
    target.PhotoImageview = finder.castView(view, 2131558677, "field 'PhotoImageview'");
    view = finder.findRequiredView(source, 2131558679, "field 'TakephotoTv'");
    target.TakephotoTv = finder.castView(view, 2131558679, "field 'TakephotoTv'");
    view = finder.findRequiredView(source, 2131558674, "field 'BackTv'");
    target.BackTv = finder.castView(view, 2131558674, "field 'BackTv'");
    view = finder.findRequiredView(source, 2131558680, "field 'ChoosePhotoTv'");
    target.ChoosePhotoTv = finder.castView(view, 2131558680, "field 'ChoosePhotoTv'");
    view = finder.findRequiredView(source, 2131558678, "field 'image_icon'");
    target.image_icon = finder.castView(view, 2131558678, "field 'image_icon'");
  }

  @Override public void unbind(T target) {
    target.SendPhotoLl = null;
    target.SendphotoButton = null;
    target.RetakeButton = null;
    target.PhotoImageview = null;
    target.TakephotoTv = null;
    target.BackTv = null;
    target.ChoosePhotoTv = null;
    target.image_icon = null;
  }
}
