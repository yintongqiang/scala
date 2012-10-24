/*
 * Copyright 2007-2010 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package crawler.lift.snippet

import collection.mutable
import crawler.lift.model.{User, Resc}
import xml.{Text, NodeSeq, Node}
import java.net.URLEncoder
import net.liftweb.http.SHtml._
import net.liftweb.http.js.JE
import net.liftweb.http.SHtml
import _root_.net.liftweb.http.SHtml._

class UserSnippet {

  // inject the date
  // replace the contents of the element with id "time" with the date
  //  def howdy = "#time *" #> date.map(_.toString)


  def generateXml: NodeSeq = {
    val list = Resc.findAll()

    var nodes = new mutable.Queue[Node]()

    for (res <- list) {
      if (res.father_id == 0) {
        var nodes2 = new mutable.Queue[Node]()
        nodes += (
          <div class="accordion-heading">
            <a class="accordion-toggle btn-info" data-toggle="collapse" data-parent="#accordion2" href={"#" + res.id.toString}>
              {res.name}
            </a>
          </div>
          )
        for (res1 <- list) {
          if (res1.father_id.toString == res.id.toString) {
            nodes2 += (<li>
              <a class="btn-link" href="#" onclick={"myPage(" + res1.id + ")"}>
                <i class="icon-hand-right"></i>{res1.name}
              </a>
            </li>)
          }
        }
        nodes += (
          <div id={res.id.toString} class="accordion-body collapse">
            <div class="row-fluid" style="height: auto;">
              <ul>
                {nodes2}
              </ul>
            </div>
          </div>
          )
      }
    }
    <div class="accordion" id="accordion2">
      <div class="accordion-group">
        {nodes}
      </div>
    </div>
  }

  def getUsers: NodeSeq = {
    val listUser = User.findAll()
    var nodes = new mutable.Queue[Node]()
    var nodes2 = new mutable.Queue[Node]()
    nodes += (<div id="actions">
      <input type="search" class="search-query" id="searchbox" name="f" value=" "
             placeholder="Filter by user name..."/>
      <input type="submit" id="searchsubmit" value="Filter by name" class="btn btn-primary"/>
      &nbsp; &nbsp; &nbsp; &nbsp;
      <a class="btn btn-info" href="#" onclick="add('username');">
        <i class="icon-plus icon-white"></i>
        添加用户</a>
      <a class="btn btn-primary" onclick="delMuti('username','1');" id="delete" href="#">删除用户</a>
      <br/>
      <br/>
    </div>)
    for (user <- listUser) {
      nodes2 += (
        <tr>
          <td>
            <input id={"ch_demo" + user.id.toString} name="ck_demo" type="checkbox" value={user.id.toString}/>
          </td>
          <td>
            {user.id.toString}
          </td>
          <td>
            {user.username.get}
          </td>
          <td>
            {user.status.get}
          </td>
          <td>
            {user.descn.get}
          </td>

          <td>
            {"role"}
          </td>

          <td>
            <a href="#" class="btn btn-info" onclick="edit('@user.id.get');">修改用户</a>
            <a href="#" class="btn btn-info" onclick="del('@user.id.get','@currentPage.page');">删除</a>
          </td>
        </tr>)
    }
    nodes += (<br/>)
    nodes += (
      <table class="table table-striped">
        <thead>
          <tr>
            <td></td>
            <th>id</th>
            <th>用户名</th>
            <th>状态</th>
            <th>描述</th>
            <th>角色权限</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          {nodes2}
        </tbody>
      </table>
      )
    <div>
      {nodes}
    </div>
  }

}
