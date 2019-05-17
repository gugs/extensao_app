package com.timsoft.meurebanho.event.model;

import com.timsoft.meurebanho.R;

public enum EnumEventType {
    ACQUISITION(false, R.drawable.enter_hl, R.drawable.enter_hd),
    BIRTH(false, R.drawable.ic_cake_hl,R.drawable.ic_cake_hd),
    DEATH(true, R.drawable.poison_filled_hl, R.drawable.poison_filled_hd),
    RETIRE(false, R.drawable.ic_delete_hl, R.drawable.ic_delete_hd),
    MILKING(true, R.drawable.milk_bottle_filled_hl, R.drawable.milk_bottle_filled_hd),
    SALE(true, R.drawable.money_bag_hl, R.drawable.money_bag_hd),
    TREATMENT(true, R.drawable.syringe_hl, R.drawable.syringe_hd),
    WEIGHING(true, R.drawable.weight_filled_hl, R.drawable.weight_filled_hd),
    CALVING(true, R.mipmap.ic_launcher, R.mipmap.ic_launcher),
    OBSERVATION(true,  R.mipmap.ic_warning_fore, R.mipmap.ic_warning_fore);

    private boolean selectable;
    private int resourceIconHL, resourceIconHD;

    EnumEventType(boolean selectable, int resourceIconHL, int resourceIconHD) {
        this.selectable = selectable;
        this.resourceIconHL = resourceIconHL;
        this.resourceIconHD = resourceIconHD;
    }

    public int getResourceIconHD() {
        return resourceIconHD;
    }

    public int getResourceIconHL() {
        return resourceIconHL;
    }

    public boolean isSelectable() {
        return selectable;
    }

}
