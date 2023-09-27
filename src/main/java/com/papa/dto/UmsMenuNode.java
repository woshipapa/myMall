package com.papa.dto;

import com.papa.mbg.model.UmsMenu;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class UmsMenuNode extends UmsMenu {
    private List<UmsMenuNode> childMenus;
}
