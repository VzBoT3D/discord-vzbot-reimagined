import { type ClassValue, clsx } from 'clsx'
import { twMerge } from 'tailwind-merge'

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}
export function formatDateToMonthYear(date: Date): string {
  return new Intl.DateTimeFormat(navigator.language, {
    month: 'long',
    year: 'numeric',
  }).format(date);
}

export function toReadableLocaleDate(isoDate: string): string {
  const date = new Date(isoDate);
  return date.toLocaleDateString(undefined, {
    year: "numeric",
    month: "long",
    day: "numeric",
    weekday: "long",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
  });
};

export const calculateReadingTime = (text: string): number => {
  const words = text.split(/\s+/).length;
  const wordsPerMinute = 200;
  return Math.ceil(words / wordsPerMinute);
};

export const formatToMinutes = (minutes: number): string => {
  if (minutes == 1) return "1 minute"
  return `${minutes} minutes`
}