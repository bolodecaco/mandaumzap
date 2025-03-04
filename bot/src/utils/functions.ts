import { WaitTimeProps } from "../@types/WaitTimeProps";
import crypto from "crypto";
import { socketVersionPage, versionFinder, versionParser } from "./global";
import { delay, WASocket } from "@whiskeysockets/baileys";

export function getBetweenValue({
  textLength,
  maxTime = 15,
  minTime = 3,
}: WaitTimeProps) {
  const baseTime = (textLength / 1000) * (maxTime - minTime) + minTime;
  const randomFactor = Math.random() * (baseTime - minTime) + minTime;
  return Math.min(randomFactor, maxTime);
}

export async function getSiteString(url: string) {
  const res = await fetch(url);
  return await res.text();
}

export async function getWhatsappSocketVersion(): Promise<
  [number, number, number]
> {
  const siteHtml = await getSiteString(socketVersionPage);
  const versionMatch = versionFinder.exec(siteHtml);
  if (!versionMatch) throw new Error("Version not found in the HTML");
  const versionString = versionMatch[0];
  const versionArray = versionString
    .split(".")
    .map((n) => Number(n.replace(/\D/g, "")));
  return [versionArray[0], versionArray[1], versionArray[2]];
}

export async function fakeTyping(socket: WASocket, jid: string) {
  await socket.sendPresenceUpdate("composing", jid);
  await delay(generateRandomValue(3000, 5000));
  await socket.sendPresenceUpdate("paused", jid);
}

function generateRandomValue(max: number, min: number) {
  return Math.random() * (max - min) + min;
}
