package core.state.util;

import core.state.models.StateModel;
import core.state.models.UserSettings;
import global.models.Favourite;
import global.utils.i18n.Language;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Хелпер для взаимодествия с конфигом
 * Created by Arsen on 09.10.2016.
 */
public class StateEditor {

    private SaveUtil saveUtil;
    private StateModel model;

    public StateEditor(SaveUtil saveUtil, StateModel model) {
        this.saveUtil = saveUtil;
        this.model = model;
    }

    private UserSettings userSettings() {
        return model.getUserSettings();
    }

    public StateEditor save() {
        saveUtil.save();
        return this;
    }


    public StateEditor setLanguage(Language lang) {
        userSettings().setLanguage(lang);
        return this;
    }

    public StateEditor addFavourite(Favourite favourite) {
        model.getListFavourite().add(favourite);
        return this;
    }

    public StateEditor addFavourite(ArrayList<Favourite> list) {
        model.getListFavourite().addAll(list);
        return this;
    }

    public StateEditor removeFavourite(Favourite favourite) {
        model.getListFavourite().remove(favourite);
        return this;
    }

    public StateEditor clearListFavourite() {
        model.getListFavourite().clear();
        return this;
    }

    public StateEditor reorderFavourites() {
        ArrayList<Favourite> listFavourite = model.getListFavourite();
        listFavourite.sort(Comparator.comparingInt(Favourite::getOrder));

        for (int i = 0; i < listFavourite.size(); i++) {
            listFavourite.get(i).setOrder(i);
        }
        return this;
    }

}
