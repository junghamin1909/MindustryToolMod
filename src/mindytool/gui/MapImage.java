package mindytool.gui;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.Texture.TextureFilter;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.Image;
import arc.util.Http;
import arc.util.Http.HttpStatus;
import arc.util.Http.HttpStatusException;
import arc.util.Log;
import arc.util.Scaling;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindytool.config.Config;
import mindytool.data.MapData;

public class MapImage extends Image {
    public float scaling = 16f;
    public float thickness = 4f;
    public Color borderColor = Pal.gray;

    public MapImage(MapData mapData) {
        super(Tex.clear);
        setScaling(Scaling.fit);

        setDrawable(Core.atlas.find("nomap"));

        Http.get(Config.IMAGE_URL + "map-previews/" + mapData.id + ".webp?format=jpeg", res -> {
            Pixmap pix = new Pixmap(res.getResult());
            Core.app.post(() -> {
                try {
                    var tex = new Texture(pix);
                    tex.setFilter(TextureFilter.linear);
                    setDrawable(new TextureRegion(tex));
                    pix.dispose();
                } catch (Exception e) {
                    Log.err(e);
                }
            });
        }, error -> {
            if (!(error instanceof HttpStatusException requestError) || requestError.status != HttpStatus.NOT_FOUND) {
                Log.err(error);
            }
        });

    }

    @Override
    public void draw() {
        super.draw();
    }
}
