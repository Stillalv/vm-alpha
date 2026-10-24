package com.phantom.killer.disabler.internal

class PeerAuthException(msg: String = "Initial authentication attempt rejected by peer.") : RuntimeException(msg)
class AdbPairingRequiredException(msg: String = "ADB pairing is required.") : Exception(msg)
