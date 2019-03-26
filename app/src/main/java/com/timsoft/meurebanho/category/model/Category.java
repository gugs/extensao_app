package com.timsoft.meurebanho.category.model;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.infra.model.IDDescription;
import java.util.ArrayList;
import java.util.List;

public class Category extends IDDescription {

    private int idCategory;

    public Category(int idCategory, String description)
    {
        super(idCategory, description);
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public static List<Category> getDefaultCategory()
    {
        List<Category> e = new ArrayList<Category>();
        e.add(new Category(1, MeuRebanhoApp.getContext().getResources().
                getString(R.string.categories_cria)));
        e.add(new Category(2, MeuRebanhoApp.getContext().getResources().
                getString(R.string.categories_recria)));
        e.add(new Category(3, MeuRebanhoApp.getContext().getResources().
                getString(R.string.categories_terminacao)));
        return e;
    }
}
