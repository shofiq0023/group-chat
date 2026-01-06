import { Client } from "./client.model";

let clients: Client[] = [];

export function addClient(username: any, sid: string): void {
    const data: Client = {
        name: username,
        sid: sid,
    };
    clients.push(data);
}

export function getClientSidByUsername(username: string): string {
    let client: Client = clients.filter((c) => c.name == username)[0];

    return client.sid;
}

export function getClientUsernameBySid(sid: string): string {
    let client: Client = clients.filter((c) => c.sid == sid)[0];

    return client.name;
}

export function getClientByUsername(username: string): Client {
    let client: Client = clients.filter((c) => c.name == username)[0];
    return client;
}

export function getClientBySid(sid: string): Client {
    let client: Client = clients.filter((c) => c.sid == sid)[0];

    return client;
}

export function getUserList(): Client[] {
    return clients;
}

export function removeClient(sid: string): void {
    clients = clients.filter((c) => c.sid != sid);
}


