interface GoogleCalendarGetResponseType {
  externalCalendars: Array<{
    calendarId: string;
    summary: string;
  }>;
}

export { GoogleCalendarGetResponseType };
