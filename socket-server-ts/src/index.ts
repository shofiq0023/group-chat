import express, { Express } from "express";
import dotenv from "dotenv";
import { createServer } from "http";
import { Server } from "socket.io";
import * as client from "./client.function";
import { Data } from "./data.model";

dotenv.config();
const app: Express = express();
const httpServer = createServer(app);
const io = new Server(httpServer, { cors: { origin: "*" } });
const port: string | undefined = process.env.PORT;

io.on("connection", (socket) => {
    let username: any = socket.handshake.query.username;
    let sid: string = socket.id;

    if (username == undefined || username == "") {
        console.error("No username provided");
        socket.disconnect(true);
    } else {
        console.info(`${username} connected`);
        client.addClient(username, sid);
        io.emit("userList", client.getUserList());
    }

    socket.on("sendMessage", (data: string) => {
        let req: Data = JSON.parse(data);
        io.to([
            client.getClientSidByUsername(username),
            client.getClientSidByUsername(req.to),
        ]).emit("getMessage", data);
    });

    socket.on("message", (data: string) => {
        io.emit("getMessage", data);
    });

    socket.on("disconnect", () => {
        client.removeClient(socket.id);
        console.error(`${sid} disconnected`);
    });
});

httpServer.listen(port, () => {
    console.log(`Server running on port ${port}`);
});
