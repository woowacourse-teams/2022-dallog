interface GoogleCalendarGetResponseType {
  externalCalendars: Array<{
    calendarId: string;
    summary: string;
  }>;
}

interface GoogleCalendarPostBodyType {
  externalId: string;
  name: string;
}

export { GoogleCalendarGetResponseType, GoogleCalendarPostBodyType };
