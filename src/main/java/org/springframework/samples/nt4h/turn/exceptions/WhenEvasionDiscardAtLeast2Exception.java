package org.springframework.samples.nt4h.turn.exceptions;

public class WhenEvasionDiscardAtLeast2Exception extends Exception {
    public WhenEvasionDiscardAtLeast2Exception() {  super("You have to discard at least 2 abilities.");
    }
}
