package com.kabasonic.notepad;

public class Methods {
    public void getTheme(String key){
        switch (key){
            case "ThemeRed":
                Constant.theme = R.style.ThemeRed;
                break;
            case "ThemePink":
                Constant.theme = R.style.ThemePink;
                break;
            case "ThemePurple":
                Constant.theme = R.style.ThemePurple;
                break;
            case "ThemeBlue":
                Constant.theme = R.style.ThemeBlue;
                break;
            case "ThemeCyan":
                Constant.theme = R.style.ThemeCyan;
                break;
            case "ThemeGreen":
                Constant.theme = R.style.ThemeGreen;
                break;
            case "ThemeDeepOrange":
                Constant.theme = R.style.ThemeDeepOrange;
                break;
            case "ThemeBrown":
                Constant.theme = R.style.ThemeBrown;
                break;
            case "AppTheme":
                Constant.theme = R.style.AppTheme;
                break;
            default:
                Constant.theme = R.style.AppTheme;
                break;
        }
    }

    public void getFonts(String key){
        switch (key){
            case "amita":
                Constant.font = R.style.fontAmita;
                break;
            case "anton":
                Constant.font = R.style.fontAnton;
                break;
            case "architects_daughter":
                Constant.font = R.style.fontArchitectsDaughter;
                break;
            case "lato":
                Constant.font = R.style.fontLato;
                break;
            case "oxygen":
                Constant.font = R.style.fontOxygen;
                break;
            case "poppins":
                Constant.font = R.style.fontPoppins;
                break;
            case "roboto":
                Constant.font = R.style.fontRoboto;
                break;
            case "rubik":
                Constant.font = R.style.fontRubik;
                break;
            default:
                Constant.font = R.style.fontRoboto;
                break;
        }
    }

    public void getTextSize(String key){
        switch (key){
            case "14":
                Constant.textSize = R.style.textSize14;
                break;
            case "16":
                Constant.textSize = R.style.textSize16;
                break;
            case "18":
                Constant.textSize = R.style.textSize18;
                break;
            case "20":
                Constant.textSize = R.style.textSize20;
                break;
            case "22":
                Constant.textSize = R.style.textSize22;
                break;
            case "24":
                Constant.textSize = R.style.textSize24;
                break;
            case "26":
                Constant.textSize = R.style.textSize26;
                break;
            default:
                Constant.textSize = R.style.textSize16;
                break;
        }
    }
}
