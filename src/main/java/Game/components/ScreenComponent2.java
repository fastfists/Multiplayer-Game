package Game.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.view.EntityView;
import javafx.scene.Node;

public class ScreenComponent2 extends Component {
    private EntityView view;

    public ScreenComponent2(EntityView view){
        this.view = view;
    }

    public ScreenComponent2(Node... nodes){
        view = new EntityView();
        view.getNodes().addAll(nodes);
    }

    @Override
    public void onAdded() {
        view.setTranslateX(entity.getX());
        view.setTranslateY(entity.getY());
    }

    @Override
    public void onUpdate(double tpf) {
        view.setRotate(entity.getRotation());
    }

    public EntityView getView() {
        return view;
    }
}
