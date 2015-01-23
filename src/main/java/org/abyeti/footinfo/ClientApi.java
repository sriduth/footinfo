package org.abyeti.footinfo;

import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api")
public class ClientApi {

    @POST
    @Path("/addTeam")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adminLogin(ApiDataModels.AddTeamModel teamData) {
        Response resp = null;
        try {
            FootDB db = new FootDB();

            db.addTeam(teamData.team_name, teamData.players.split(";"));
            resp = Response.status(200).entity("{\"STATUS\" : \"SUCCEEDED\"}").build();

        }
        catch (Exception e){
            e.printStackTrace();
            resp = Response.status(503).entity("{\"STATUS\" : \"FAILED\"}").build();
        }

        return resp;
    }

    @POST
    @Path("/createGame")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGame(ApiDataModels.CreateGameModel gameData) {
        JSONObject responseData = new JSONObject();

        Response response = Response
                .status(200)
                .entity(responseData.append("STATUS", "SUCCEEDED").toString())
                .build();


        try {
            FootDB db = new FootDB();
            if(!db.createGame(gameData.teamA, gameData.teamB)) {
                Response
                        .status(404)
                        .entity(responseData.append("STATUS", "No Such Team!").toString())
                        .build();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            Response
                    .status(502)
                    .entity(responseData.append("STATUS", "FAILED").toString())
                    .build();
        }

        return response;
    }

    @GET
    @Path("/getGameStatus")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllGameStatus() {
        JSONObject gameSatuses = new JSONObject();

        Response resp = null;

        try {
            FootDB db = new FootDB();
            gameSatuses.accumulate("game_statuses", db.getGameStatuses());
            gameSatuses.accumulate("status", "suceeded");
            resp = Response
                    .status(200)
                    .entity(gameSatuses.toString())
                    .build();
        }
        catch(Exception e) {
            e.printStackTrace();
            resp = Response
                    .status(500)
                    .entity(new JSONObject().append("status", "failed").toString())
                    .build();
        }

        return resp;
    }

    @GET
    @Path("/getAllPlayers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTeams() {
        Response resp = null;
        JSONObject data = new JSONObject();
        try {
            FootDB db = new FootDB();
            data.accumulate("players", db.getAllPlayers());
            data.accumulate("status", "succeeded");
            resp = Response.status(200).entity(data.toString()).build();
        }
        catch (Exception e) {
            e.printStackTrace();
            resp = Response.status(500).entity("{\"status\" : \"failed\"}").build();
        }
        return resp;
    }

    @POST
    @Path("/finishGame")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response stopGame(ApiDataModels.FinishGameModel gameData) {
        Response resp = null;
        try {
            FootDB db = new FootDB();
            if(db.stopGame(gameData.matchId)) {
                resp = Response.status(200).entity("{\"status\" : \"succeeded\" }").build();
            }
            else
                resp = Response.status(200).entity("{\"status\" : \"failed\" }").build();
        }
        catch (Exception e) {
            e.printStackTrace();
            resp = Response.status(500).entity("{\"status\" : \"failed\" }").build();
        }
        return resp;
    }
}