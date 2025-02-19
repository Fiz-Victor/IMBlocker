package io.github.reserveword.imblocker.mixin;

import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.reserveword.imblocker.IMCheckState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = WTextField.class, remap = false)
public abstract class CottonTextFieldMixin {
    @Unique
    protected boolean editable;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickCallback(CallbackInfo ci) {
        IMCheckState.captureTick(this, this.editable);
    }

    @Inject(method = "onCharTyped", at = @At("HEAD"), cancellable = true)
    public void charTypedCallback(char ch, CallbackInfoReturnable<InputResult> cir) {
        if (IMCheckState.captureNonPrintable(this, ch, this.editable)) {
            cir.setReturnValue(InputResult.IGNORED);
        }
    }
}
