<html>
  <head>
    <meta charset="utf-8" />
    <meta content="IE=edge" http-equiv="X-UA-Compatible" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
   <style type="text/css"><#include "./editTeam.css"></style>
    <title>maljae home</title>
  </head>

  <body>
    <div class="container">
      <p id="teamName">Information about</p>
      <form action="/team/update/${token}" method="post">
        <div>
          <label for="email">Enter your secret: </label>
          <input
            class="input"
            type="string"
            name="secret"
            id="secret"
            required
          />
        </div>
        <div>
          <label for="memberGrid">Enter the members of your team</label>
          <input
            id="email_input"
            type="email"
            name="email"
            class="input"
            placeholder="enter new email"
          />
          <div id="memberGrid"></div>
        </div>
        <div>
          <label for="preferences">Team Preferences : </label>
          <div id="preferences"></div>
        </div>
        <div>
          <button class="input" id="submit" type="submit">
            update
          </button>
        </div>
      </form>

      <ul class="bg-bubbles">
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
      </ul>
    </div>
    <script
      src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"
      integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
      crossorigin="anonymous"
    ></script>
    <script type="text/javascript" src="app.js"></script>
  </body>
</html>
