package io.tomneh.canvengine.res;

import java.io.Serializable;

public record ImgProperties(String name, int width, int height, int rotInDegrees) implements Serializable {}
