import { useState } from 'react';
import { DemoContainer } from '@mui/x-date-pickers/internals/demo';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import 'dayjs/locale/ru'
import GetRequest from '../rest/GetRequest';

const API_URL = process.env.REACT_APP_API_URL

const ReservationsById = () => {
  const [date, setDate] = useState(null);
  const { data, setData, loading, error, setError, getData } = GetRequest(API_URL)

  const handleButtonClick = () => {
    if (date != null){
      const normDate = date.toISOString()
      getData(`/reservations?date=${normDate}`)
    } else {
      setError(new Error("Date and time must not be empty"));
      setData(null);
    }
  };

  return (
    <div>
        <p>/reservations?date={`{date}`}</p>
        <p>Enter date</p>
        <div style={{width : "fit-content"}}>
            <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale="ru">
                <DemoContainer components={['DateTimePicker']}>
                    <DateTimePicker
                        views={['year', 'month', 'day', 'hours', 'minutes', 'seconds']}
                        label="Date and time"
                        value={date}
                        onChange={(e) => setDate(e)}
                    />
                </DemoContainer>
            </LocalizationProvider>
        </div>
        <button onClick={handleButtonClick}>Fetch Data</button>
        <div>
          {loading && <p>Loading...</p>}
          {error && <p>{error.message}</p>}
          {data && <pre>{JSON.stringify(data, null, 2)}</pre>}
        </div>
    </div>
  );
};

export default ReservationsById;