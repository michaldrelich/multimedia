using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ARPlaneChanger : MonoBehaviour
{

    public Material[] mats;
    private int pos = 0;
    private int oldPos = 0;
    private int counter = 0;
    private bool cond = false;

    public MeshRenderer currentGroundPlane;

    // Start is called before the first frame update
    void Start()
    {

    }

    // Update is called once per frame
    void Update()
    {

    }

    public void NextPlane()
    {

        if (counter >= 1)
        {
            oldPos = pos;
        }
        counter = counter + 1;

        pos = pos + 1;
        if (pos >= 8)
        {
            pos = 0;
        }
        cond = true;
        SetPosTo(pos);
        cond = false;

    }

    public void SetPosTo(int newPos)
    {
        if (cond == false)
        {
            oldPos = pos;
        }

        pos = newPos;

        MeshRenderer[] rends = transform.GetComponentsInChildren<MeshRenderer>();
        for (int i = 0; i < rends.Length; i++)
        {
            rends[i].material = mats[pos];
        }

        currentGroundPlane.material = mats[pos];
    }

    public void Undo()
    {
        pos = oldPos;

        cond = true;
        SetPosTo(pos);
        cond = false;
    }
}