package app.revanced.patches.bilibili.misc.json.fingerprints

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint

object PegasusParserFingerprint : MethodFingerprint(
    strings = listOf("card_type is empty"),
)
