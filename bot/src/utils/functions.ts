import { WaitTimeProps } from "../@types/WaitTimeProps";
import crypto from "crypto";
import { socketVersionPage, versionFinder, versionParser } from "./global";

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
  const versionParse = versionParser.exec(versionString);
  if (!versionParse) throw new Error("Failed to parse version");
  const versionArray = versionParse[0].split(".");
  if (versionArray.length !== 3) {
    throw new Error("Version must have exactly 3 parts");
  }
  return [
    parseInt(versionArray[0]),
    parseInt(versionArray[1]),
    parseInt(versionArray[2]),
  ];
}

export function generateHashToken() {
  return crypto
    .createHash("sha256")
    .update(crypto.randomBytes(32))
    .digest("hex");
}
