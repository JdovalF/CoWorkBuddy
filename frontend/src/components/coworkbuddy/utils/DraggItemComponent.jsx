
import { useDrag } from "react-dnd";
import React from 'react';

const DraggItemComponent = ({ worker }) => {
    console.log('Worker:', worker);
    console.log("Entrando al componente de arrastre");
  
    const [, drag] = useDrag({
      type: 'WORKER',
      item: { type: 'WORKER', id: worker.id, name: worker.name, active: worker.active }
    });

    return (
      <div ref={drag} className="chip-worker">
        {worker.name}
      </div>
    );
  }
  
  export default DraggItemComponent;