/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

/**
 * SelectionObserver - enables a class to register itself to be informed of selection events.
 * 
 * @author Eric McCreath
 *
 */

public interface SelectionObserver {
	void update();
}
