package controllers;

import play.i18n.Messages;

public class LocalizedException extends Exception {

  public LocalizedException(String code) {
    super(Messages.get(code));
  }

}
