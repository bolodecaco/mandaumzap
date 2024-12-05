import { WaitTimeProps } from "../@types/WaitTimeProps";

export function getBetweenValue({
  textLength,
  maxTime = 15,
  minTime = 3,
}: WaitTimeProps) {
  const baseTime = (textLength / 1000) * (maxTime - minTime) + minTime;
  const randomFactor = Math.random() * (baseTime - minTime) + minTime;
  return Math.min(randomFactor, maxTime);
}
