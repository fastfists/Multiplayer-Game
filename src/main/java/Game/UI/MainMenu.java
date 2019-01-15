package Game.UI;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.scene.FXGLMenu;
import com.almasb.fxgl.scene.menu.MenuType;
import com.almasb.fxgl.settings.ReadOnlyGameSettings;
import com.almasb.fxgl.ui.FontFactory;
import com.almasb.fxgl.util.Language;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import static Game.GameApp.globalSettings;

/*
 * To-do list area...
 * TODO: Think about maybe implementing a way for the user to submit their own profile icon rather than using built-in icons.
 * TODO: Maybe use this code somewhere?
        Image backgroundImage = FXGL.getAssetLoader().loadImage("BulletHailPattern.png");
        mainLayout.setBackground(new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.ROUND, BackgroundRepeat.ROUND, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
 */
public class MainMenu extends FXGLMenu {
    final FontFactory OVERPASS_LIGHT_FACTORY = FXGL.getAssetLoader().loadFont("overpass/overpass-light.otf");
    final FontFactory OVERPASS_LIGHT_ITALIC_FACTORY = FXGL.getAssetLoader().loadFont("overpass/overpass-light-italic.otf");
    final FontFactory OVERPASS_REGULAR_FACTORY = FXGL.getAssetLoader().loadFont("overpass/overpass-regular.otf");
    final FontFactory OVERPASS_HEAVY_ITALIC_FACTORY = FXGL.getAssetLoader().loadFont("overpass/overpass-heavy-italic.otf");
    final FontFactory OVERPASS_MONO_REGULAR_FACTORY = FXGL.getAssetLoader().loadFont("overpass-mono/overpass-mono-regular.otf");
    final FontFactory HACK_REGULAR_FACTORY = FXGL.getAssetLoader().loadFont("hack/Hack-Regular.ttf");
    final FontFactory ASAP_SEMIBOLD_FACTORY = FXGL.getAssetLoader().loadFont("asap/Asap-SemiBold.ttf");
    final FontFactory ASAP_MEDIUM_FACTORY = FXGL.getAssetLoader().loadFont("asap/Asap-Medium.ttf");
    final FontFactory ASAP_REGULAR_FACTORY = FXGL.getAssetLoader().loadFont("asap/Asap-Regular.ttf");
    final double heightRatio = app.getHeight() / 600.0;
    final double widthRatio = app.getWidth() / 900.0;
    final double fontRatio = 25.0 / 6;
    final double defactoRatio = (app.getHeight() < app.getWidth()) ? heightRatio : widthRatio; // Actual ratio to be used in scaling menu elements.
    final ReadOnlyGameSettings settings = app.getSettings();
    final GridPane mainMenuLayout;

    public MainMenu(GameApplication app) {
        super(app, MenuType.MAIN_MENU);


        //// Super class adds unwanted nodes to root, so these instructions rids of those unwanted nodes
        //// while maintaining menuRoot and contentRoot.
        getRoot().getChildren().clear();
        getRoot().getChildren().add(menuRoot);
        getRoot().getChildren().add(getContentRoot());

        //// Adjusting the overall layout of the main menu
        double marginSize = 42 * defactoRatio;
        mainMenuLayout = new GridPane();
        RowConstraints row = new RowConstraints(app.getHeight() / 2.0 - marginSize);
        ColumnConstraints col = new ColumnConstraints(app.getWidth() / 2.0 - marginSize);
        mainMenuLayout.getRowConstraints().addAll(row, row);
        mainMenuLayout.getColumnConstraints().addAll(col, col);
        mainMenuLayout.setPadding(new Insets(marginSize));
        //mainMenuLayout.setBackground(new Background(new BackgroundFill(Color.rgb(230, 224, 211),CornerRadii.EMPTY, Insets.EMPTY)));


        //// Make header and add it to main layout
        Text title = new Text(settings.getTitle());
        title.setFont(OVERPASS_HEAVY_ITALIC_FACTORY.newFont(12 * fontRatio * defactoRatio));

        Text version = new Text(settings.getVersion());
        version.setFont(OVERPASS_LIGHT_ITALIC_FACTORY.newFont(4 * fontRatio * defactoRatio));
        version.setFill(Color.rgb(120, 120, 120));

        VBox header = new VBox(title, version);
        header.setSpacing(-9 * defactoRatio);

        mainMenuLayout.add(header, 0, 0);


        //// Make TextButtons on left side of menu and add it to main layout
        VBox textButtons = new VBox();
        textButtons.setSpacing(18 * defactoRatio);
        textButtons.setAlignment(Pos.BOTTOM_LEFT);

        Font overpassReg = OVERPASS_REGULAR_FACTORY.newFont(12 * fontRatio * defactoRatio);
        Color orange = Color.rgb(255, 179, 71);
        TextButton play = new TextButton("Play", overpassReg, orange, this::fireNewGame);
        TextButton modifyCars = new TextButton("Modify Cars", overpassReg, orange, this::doNothing);

        Font overpassLight = OVERPASS_LIGHT_FACTORY.newFont(10 * fontRatio * defactoRatio);
        Color blue = Color.rgb(41, 128, 187);
        TextButton settings = new TextButton("Settings", overpassLight, blue, this::showSettingsPage);
        TextButton changeProfile = new TextButton("Change profile", overpassLight, blue, this::fireLogout);
        TextButton exitGame = new TextButton("Exit Game", overpassLight, blue, this::fireExit);
        textButtons.getChildren().addAll(play, modifyCars, settings, changeProfile, exitGame);

        GridPane.setValignment(textButtons, VPos.BOTTOM);
        mainMenuLayout.add(textButtons, 0, 1);


        //// Make profile area that is in the top right area of the main menu
        HBox profileArea = new HBox();
        profileArea.setSpacing(11 * defactoRatio);
        profileArea.setAlignment(Pos.TOP_RIGHT);

        Text profileName = new Text();
        profileName.setFont(HACK_REGULAR_FACTORY.newFont(5 * fontRatio * defactoRatio));
        profileName.setTextAlignment(TextAlignment.RIGHT);
        profileName.setTranslateY(7 * defactoRatio);
        listener.profileNameProperty().addListener((o, oldValue, newValue) -> profileName.setText(newValue));

        Rectangle iconBox = new Rectangle(37 * defactoRatio, 37 * defactoRatio, null);
        iconBox.setStroke(Color.BLACK);
        Image profilePicture = FXGL.getAssetLoader().loadImage("Profile pictures/tire.png"); // TODO: Implement way for player to use different profile picture...
        iconBox.setFill(new ImagePattern(profilePicture));

        profileArea.getChildren().addAll(profileName, iconBox);

        GridPane.setHalignment(profileArea, HPos.RIGHT);
        mainMenuLayout.add(profileArea, 1, 0);


        //// Make the "What's New?" button on the bottom right of the main menu
        BoxButtonSettings whatsNewConfig = new BoxButtonSettings();
        whatsNewConfig.text = "What's new?";
        whatsNewConfig.vMargin = 4 * defactoRatio;
        whatsNewConfig.hMargin = 13 * defactoRatio;
        whatsNewConfig.font = OVERPASS_LIGHT_FACTORY.newFont(4 * fontRatio * defactoRatio);
        whatsNewConfig.setBackgroundAndTextColors(Color.hsb(180, 1, 0.5));

        BoxButton whatsNewPane = new BoxButton(whatsNewConfig);

        GridPane.setHalignment(whatsNewPane, HPos.RIGHT);
        GridPane.setValignment(whatsNewPane, VPos.BOTTOM);
        mainMenuLayout.add(whatsNewPane, 1, 1);

        menuRoot.getChildren().add(mainMenuLayout);
    }

    /**
     * This convenience method is intended to be used for places in which a lambda must be specified with empty
     * parameters and nothing should be done in that lambda.
     */
    private void doNothing() {
        // Do nothing
    }

    /**
     * This convenience method is intended to be used for places in which a consumer needs to specified and nothing
     * should be done with the consumable. A benefit of this method is that it has an descriptive name
     *
     * @param consumable Consumable object to have nothing done with.
     */
    private void blackHole(Object... consumable) {
        // Do nothing
    }

    public class TextButton extends Text {
        public TextButton(String text, Font font, Color normCol, Color otherCol, Runnable action) {
            super(text);
            this.setFont(font);
            this.setFill(normCol);
            this.setOnMouseEntered(event -> this.setFill(otherCol));
            this.setOnMouseExited(event -> this.setFill(normCol));
            this.setOnMouseClicked(event -> action.run());
        }

        public TextButton(String text, Font font, Color otherCol, Runnable action) {
            this(text, font, Color.BLACK, otherCol, action);
        }
    }

    private void showSettingsPage() {
        final double margin = 48 * defactoRatio;

        VBox page = new VBox();
        page.setTranslateY(-9 * defactoRatio);
        page.setPadding(new Insets(margin));
        page.setPrefWidth(app.getWidth());
        page.setSpacing(25 * defactoRatio);

        //// Make header
        Text header = new Text("Settings");
        header.setFont(ASAP_SEMIBOLD_FACTORY.newFont(margin));

        //// Make pane which contains all of the settings for the user to change and/or look at.
        ScrollPane content = new ScrollPane();
        content.setId("TransparentScrollPane");
        content.setPrefSize(app.getWidth() - margin * 4, 2 * app.getHeight() / 3.0);
        content.setMaxSize(app.getWidth() - margin * 4, 2 * app.getHeight() / 3.0);
        content.setPadding(new Insets(0, 0, 0, margin));

        VBox sections = new VBox();

        SettingSection videoSection = new SettingSection("Video");

        SettingField resolution = new SettingField("Resolution:", getResolutionChoiceBox());
        CheckBox fullscreenCb = new CheckBox();
        fullscreenCb.selectedProperty().bindBidirectional(settings.getFullScreen());
        SettingField fullscreen = new SettingField("Fullscreen:", fullscreenCb);
        videoSection.addFields(resolution, fullscreen);

        SettingSection audioSection = new SettingSection("Audio");
        SettingField musicVolume = getAudioSlider("Music volume:", app.getAudioPlayer().globalMusicVolumeProperty());
        SettingField soundVolume = getAudioSlider("Sound volume:", app.getAudioPlayer().globalSoundVolumeProperty());
        audioSection.addFields(musicVolume, soundVolume);

        SettingSection controlSection = new SettingSection("Controls");
        // TODO: Implement remappable controls

        SettingSection miscSection = new SettingSection("Misc.");

        ChoiceBox<Language> languageCb = new ChoiceBox<>(FXCollections.observableArrayList(Language.values()));
        languageCb.valueProperty().bindBidirectional(settings.getLanguage());
        SettingField language = new SettingField("Language:", languageCb);

        Label creditsLabel = new Label(String.join(", ", FXGL.getSettings().getCredits().getList()));
        creditsLabel.setWrapText(true);
        creditsLabel.setMaxWidth(app.getWidth() - 8 * margin);
        creditsLabel.setPrefWidth(app.getWidth() - 8 * margin);
        SettingField credits = new SettingField("Credits:", creditsLabel, Pos.TOP_CENTER);

        String playtimeStr = app.getGameplay().getStats().getPlaytimeHours() + "H " +
                app.getGameplay().getStats().getPlaytimeMinutes() + "M " +
                app.getGameplay().getStats().getPlaytimeSeconds() + "S";
        Text playtimeText = new Text(playtimeStr);
        SettingField playtime = new SettingField("Total Playtime:", playtimeText);

        CheckBox introEnabledCb = new CheckBox();
        introEnabledCb.setSelected(globalSettings.isIntroEnabled());
        introEnabledCb.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            try {
                globalSettings.setIntroEnabled(newValue);
            } catch (IOException e) {
                FXGL.getDisplay().showConfirmationBox(e.getMessage(), this::blackHole);
            }
        }));
        SettingField introEnabled = new SettingField("Intro enabled:", introEnabledCb);

        miscSection.addFields(language, credits, playtime, introEnabled);

        sections.getChildren().addAll(videoSection, audioSection, controlSection, miscSection);

        content.setContent(sections);

        BoxButtonSettings backButtonConfig = new BoxButtonSettings();
        backButtonConfig.hMargin = 8 * defactoRatio;
        backButtonConfig.vMargin = 8 * defactoRatio;
        backButtonConfig.text = "Go Back";
        backButtonConfig.action = this::returnToMainMenu;
        backButtonConfig.setBackgroundAndTextColors(Color.BLACK);

        BoxButton backButton = new BoxButton(backButtonConfig);

        HBox backButtonWrap = new HBox(backButton);
        backButtonWrap.setAlignment(Pos.BOTTOM_RIGHT);

        page.getChildren().addAll(header, content, backButtonWrap);

        switchMenuContentTo(page);
    }

    private SettingField getAudioSlider(String label, Property<Number> prop) {
        Slider slider = new Slider(0, 1, 1);
        slider.valueProperty().bindBidirectional(prop);
        Text percentSound = new Text();
        percentSound.textProperty().bind(slider.valueProperty().multiply(100).asString("%.0f%%"));
        HBox sliderBox = new HBox(slider, percentSound);
        sliderBox.setSpacing(5 * defactoRatio);
        sliderBox.setAlignment(Pos.CENTER);
        return new SettingField(label, sliderBox);
    }

    private ChoiceBox getResolutionChoiceBox() {
        Resolution currentRes = new Resolution(globalSettings.getWidthRes(), globalSettings.getHeightRes());
        ArrayList<Resolution> resolutions = new ArrayList<>(getDefaultResolutionsAnd(currentRes));
        ChoiceBox<Resolution> resChoices = new ChoiceBox<>(FXCollections.observableArrayList(resolutions));
        resChoices.getSelectionModel().select(currentRes);
        resChoices.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            Resolution chosenRes = resolutions.get(newValue.intValue());
            try {
                globalSettings.setWidthRes(chosenRes.width);
                globalSettings.setHeightRes(chosenRes.height);
                FXGL.getDisplay().showConfirmationBox("Success! Please restart the game to have this change take effect.", this::blackHole);
            } catch (IOException e) {
                FXGL.getDisplay().showConfirmationBox("Error! Could not write change to disk.", this::blackHole);
            }
        });
        return resChoices;
    }

    private HashSet<Resolution> getDefaultResolutionsAnd(Resolution currentRes) {
        HashSet<Resolution> resolutions = new HashSet<>();
        resolutions.add(new Resolution(1920, 1080));
        resolutions.add(new Resolution(1366, 768));
        resolutions.add(new Resolution(900, 600));
        resolutions.add(currentRes);
        return resolutions;
    }

    private void returnToMainMenu() {
        getContentRoot().getChildren().clear();
        switchMenuTo(mainMenuLayout);
    }

    @Override
    protected void switchMenuTo(Node menuNode) {
        menuRoot.getChildren().clear();
        menuRoot.getChildren().add(menuNode);
    }

    @Override
    protected void switchMenuContentTo(Node content) {
        menuRoot.getChildren().clear();
        getContentRoot().getChildren().clear();
        getContentRoot().getChildren().add(content);
    }

    private class SettingField extends HBox {
        SettingField(String fieldText, Node content, Pos textPos) {
            Text text = new Text(fieldText);
            text.setFont(ASAP_REGULAR_FACTORY.newFont(18 * defactoRatio));

            VBox textWrap = new VBox(text);
            textWrap.setAlignment(textPos);

            content.setStyle("-fx-font: 16px \"Overpass mono\""); // TODO: Remember to make sure user installs this font...

            HBox contentWrap = new HBox(content);
            contentWrap.setAlignment(textPos);
            contentWrap.setMinHeight(textWrap.getMaxHeight());
            contentWrap.setPrefHeight(textWrap.getMaxHeight());
            contentWrap.setMaxHeight(textWrap.getMaxHeight());

            this.setSpacing(8 * defactoRatio);
            this.getChildren().addAll(textWrap, contentWrap);
        }

        SettingField(String fieldText, Node content) {
            this(fieldText, content, Pos.CENTER);
        }
    }

    private class Resolution {
        int width;
        int height;

        Resolution(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Resolution))
                return false;
            Resolution other = (Resolution) o;
            return this.width == other.width &&
                    this.height == other.height;
        }

        @Override
        public String toString() {
            return width + "x" + height;
        }
    }

    private class SettingSection extends VBox {
        private VBox videoSubSection;

        SettingSection(String headerText) {
            Text videoHeader = new Text(headerText);
            videoHeader.setFont(ASAP_MEDIUM_FACTORY.newFont(30 * defactoRatio));
            videoHeader.setUnderline(true);

            this.videoSubSection = new VBox();
            videoSubSection.setPadding(new Insets(12 * defactoRatio, 0, 15 * defactoRatio, 48 * defactoRatio));
            videoSubSection.setSpacing(10 * defactoRatio);

            this.getChildren().addAll(videoHeader, videoSubSection);
        }

        void addFields(Node... settingFields) {
            this.videoSubSection.getChildren().addAll(settingFields);
        }
    }

    @Override
    protected Button createActionButton(String name, Runnable action) {
        Button btn = new Button(name);
        btn.addEventHandler(ActionEvent.ACTION, event -> action.run());
        return btn;
    }

    @Override
    protected Button createActionButton(StringBinding name, Runnable action) {
        return createActionButton(name.getValue(), action);
    }

    @Override
    protected Node createBackground(double width, double height) {
        Rectangle bg = new Rectangle();
        bg.setFill(Color.RED);
        return bg;
    }

    @Override
    protected Node createTitleView(String title) {
        return FXGL.getUIFactory().newText(title);
    }

    @Override
    protected Node createVersionView(String version) {
        return FXGL.getUIFactory().newText(version);
    }

    @Override
    protected Node createProfileView(String profileName) {
        return FXGL.getUIFactory().newText(profileName);
    }

}