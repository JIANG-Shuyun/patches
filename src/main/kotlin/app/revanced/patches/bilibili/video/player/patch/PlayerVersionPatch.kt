package app.revanced.patches.bilibili.video.player.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod
import app.revanced.patches.bilibili.annotations.BiliBiliCompatibility
import app.revanced.patches.bilibili.video.player.fingerprints.FFUniteDetailAbFingerprint

@Patch
@BiliBiliCompatibility
@Name("player-version")
@Description("播放器版本")
class PlayerVersionPatch : BytecodePatch(listOf(FFUniteDetailAbFingerprint)) {
    override fun execute(context: BytecodeContext): PatchResult {
        fun MutableMethod.patch() = addInstructionsWithLabels(
            0, """
            invoke-static {}, Lapp/revanced/bilibili/patches/PlayerVersionPatch;->playerVersion()I
            move-result v0
            if-eqz v0, :jump
            const/4 v1, 0x2
            if-eq v0, v1, :new
            const/4 v0, 0x0
            goto :return
            :new
            const/4 v0, 0x1
            :return
            return v0
            :jump
            nop
        """.trimIndent()
        )
        // < 7.39.0
        context.findClass("Ltv/danmaku/biliplayerv2/GeminiPlayerFFKt;")?.let { clazz ->
            clazz.mutableClass.methods.find { it.returnType == "Z" && it.parameters.isEmpty() }?.patch()
        }
        // >= 7.39.0
        FFUniteDetailAbFingerprint.result?.mutableMethod?.patch()
        return PatchResultSuccess()
    }
}
