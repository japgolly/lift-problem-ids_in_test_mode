package demo

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object DemoPage {

  val Component = ScalaComponent.static("Demo", {

    val map = GoogleMap.Component(GoogleMap.props(137.0537453, -35.8177544, 9))()

    <.div(
      <.h1("Demo"),
      <.div(^.height := 360.px, ^.width := 90.pct, map))
  })

}
