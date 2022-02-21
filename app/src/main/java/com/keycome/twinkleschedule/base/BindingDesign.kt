package com.keycome.twinkleschedule.base

import androidx.viewbinding.ViewBinding
import com.keycome.twinkleschedule.extension.BindingWrapper

abstract class BindingDesign<VB : ViewBinding> : Design(), BindingWrapper<VB>
