// import apiService from "./service/apiService";

// const dataProvider = {
//   getList: async (resource, params) => {
//     const response = await apiService.get(`/${resource}`);
//     return { data: response, total: response.length }; // ✅ Ensure data + total count
//   },

//   getOne: async (resource, params) => {
//     const response = await apiService.get(`/${resource}/${params.id}`);
//     return { data: response }; // ✅ Ensure data object
//   },

//   create: async (resource, params) => {
//     console.log("createdata params ", params);
//     console.log("createdata resource ", resource);
    
//     const response = await apiService.post(`/${resource}`, params.data);
//     return { data: { ...params.data, id: response.id } }; // ✅ Include `id`
//   },

//   update: async (resource, params) => {
//     const response = await apiService.put(`/${resource}/${params.id}`, params.data);
//     return { data: response }; // ✅ Ensure response has `data`
//   },

//   delete: async (resource, params) => {
//     await apiService.delete(`/${resource}/${params.id}`);
//     return { data: { id: params.id } }; // ✅ Return deleted `id`
//   },
// };

// export default dataProvider;




import apiService from "./service/apiService";

const dataProvider = {
  getList: async (resource, params) => {
    console.log(`Fetching list for resource: ${resource}`);

    // Handle user-specific API
    let url = `/${resource}`;
    if (resource === "users") url = "/users/all";

    try {
      const response = await apiService.get(url);
      return { data: response, total: response.length }; // Ensure data + total count
    } catch (error) {
      console.error(`Error fetching ${resource} list:`, error);
      throw error;
    }
  },

  getOne: async (resource, params) => {
    console.log(`Fetching one ${resource} with ID: ${params.id}`);

    try {
      const response = await apiService.get(`/${resource}/${params.id}`);
      return { data: response }; // Ensure response has data
    } catch (error) {
      console.error(`Error fetching ${resource} with ID ${params.id}:`, error);
      throw error;
    }
  },

  create: async (resource, params) => {
    console.log("Creating new entry for:", resource, params);

    try {
      const response = await apiService.post(`/${resource}`, params.data);
      return { data: { ...params.data, id: response.id } }; // Ensure response includes `id`
    } catch (error) {
      console.error(`Error creating ${resource}:`, error);
      throw error;
    }
  },

  update: async (resource, params) => {
    console.log(`Updating ${resource} with ID: ${params.id}`, params.data);

    // Handle user-specific update API
    let url = `/${resource}/${params.id}`;
    if (resource === "users") url = "/users/update";

    try {
      const response = await apiService.put(url, params.data);
      return { data: response }; // Ensure response has data
    } catch (error) {
      console.error(`Error updating ${resource} with ID ${params.id}:`, error);
      throw error;
    }
  },

  delete: async (resource, params) => {
    console.log(`Deleting ${resource} with ID: ${params.id}`);

    // Handle user-specific delete API
    let url = `/${resource}/${params.id}`;
    if (resource === "users") url = "/users/delete";

    try {
      await apiService.delete(url, { id: params.id });
      return { data: { id: params.id } }; // Ensure deleted ID is returned
    } catch (error) {
      console.error(`Error deleting ${resource} with ID ${params.id}:`, error);
      throw error;
    }
  },
};

export default dataProvider;
