import React from 'react';
import PostRequest from '../rest/PostRequest';
import { DemoContainer } from '@mui/x-date-pickers/internals/demo';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import 'dayjs/locale/ru';

const API_URL = process.env.REACT_APP_API_URL

const Acquire = () => {
  const { formData, data, loading, error, setFormData, postData } = PostRequest(
    {
      user: { name : '' },
      resource: { name : '' },
      start: null,
      end: null
    },
    API_URL
  );

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name.startsWith("user") || name.startsWith("resource")) {
      const [obj, prop] = name.split(".");
      setFormData({
        ...formData,
        [obj]: {
          ...formData[obj],
          [prop]: value
        }
      });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };
  

  const handleSubmit = (e) => {
    e.preventDefault();
    postData('/resources/acquire');
  };

  return (
    <div>
      <p>Резервирование ресурса</p>
      <p>POST /resources/acquire (используйте имя авторизованного в данный момент пользователя)</p>
      <form onSubmit={handleSubmit}>
        <label>
          <p>user</p>
          <input type="text" name="user.name" value={formData.user.name} onChange={handleChange} />
        </label>
        <label>
          <p>resource</p>
          <input type="text" name="resource.name" value={formData.resource.name} onChange={handleChange} />
        </label>
        <label>
          <p>start</p>
          <div style={{ width: "fit-content" }}>
            <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale="ru">
              <DemoContainer components={['DateTimePicker']}>
                <DateTimePicker
                  views={['year', 'month', 'day', 'hours', 'minutes', 'seconds']}
                  label="Date and time"
                  value={formData.start}
                  onChange={(e) => setFormData({ ...formData, 'start': e })}
                />
              </DemoContainer>
            </LocalizationProvider>
          </div>
        </label>
        <label>
          <p>end</p>
          <div style={{ width: "fit-content" }}>
            <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale="ru">
              <DemoContainer components={['DateTimePicker']}>
                <DateTimePicker
                  views={['year', 'month', 'day', 'hours', 'minutes', 'seconds']}
                  label="Date and time"
                  value={formData.end}
                  onChange={(e) => setFormData({ ...formData, 'end': e })}
                />
              </DemoContainer>
            </LocalizationProvider>
          </div>
        </label>
        <p><button type="submit">Send</button></p>
      </form>
      <div>
        {loading && <p>Loading...</p>}
        {error && <p>{error.message}</p>}
        {data && <pre>{JSON.stringify(data, null, 2)}</pre>}
      </div>
    </div>
  );
};

export default Acquire;
