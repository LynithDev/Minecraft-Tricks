package com.example

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.Framebuffer
import net.minecraft.client.gl.SimpleFramebuffer
import org.jetbrains.skia.*
import org.lwjgl.opengl.GL33.*

object Renderer {
    private val mc = MinecraftClient.getInstance()

    private lateinit var context: DirectContext
    private lateinit var renderTarget: BackendRenderTarget
    lateinit var surface: Surface
    lateinit var canvas: Canvas
    lateinit var targetFbo: Framebuffer

    fun render() {
        val outputFb = glGetInteger(GL_DRAW_FRAMEBUFFER_BINDING)

        UIState.backup()
        context.resetGLAll()

        canvas.drawCircle(100f, 100f, 50f, Paint().also {
            it.color = 0xFFFF0000.toInt()
        })

        context.flush()
        UIState.restore()

        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, outputFb)
        targetFbo.draw(mc.window.width, mc.window.height, false)
    }

    // Initialize this once the window is created
    fun initContext() {
        initFbo()
        context = DirectContext.makeGL()
    }

    // Self documenting code
    fun onResize() {
        initSkia()

        targetFbo.delete()
        initFbo()
    }

    // Self documenting code v2
    private fun initFbo() {
        targetFbo = SimpleFramebuffer(mc.window.width, mc.window.height, true, false)
    }

    fun initSkia() {
        // Close the previous surface and render target
        if (this::surface.isInitialized)
            surface.close()

        // Close the previous render target
        if (this::renderTarget.isInitialized)
            renderTarget.close()

        // Create a new render target and surface
        renderTarget = BackendRenderTarget.makeGL(
            mc.window.width,
            mc.window.height,
            0,
            8,
            glGetInteger(GL_FRAMEBUFFER_BINDING),
            FramebufferFormat.GR_GL_RGBA8
        )

        surface = Surface.makeFromBackendRenderTarget(
            context,
            renderTarget,
            SurfaceOrigin.BOTTOM_LEFT,
            SurfaceColorFormat.RGBA_8888,
            ColorSpace.sRGB,
        )!!

        canvas = surface.canvas
    }
}